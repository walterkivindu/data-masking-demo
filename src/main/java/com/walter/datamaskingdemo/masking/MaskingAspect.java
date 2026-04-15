package com.walter.datamaskingdemo.masking;

import com.walter.datamaskingdemo.annotation.masking.Mask;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Aspect
@Component
public class MaskingAspect {

    private final ConcurrentMap<Class<?>, Field[]> fieldCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<Class<?>, Boolean> hasMaskedFieldsCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<Class<?>, java.lang.reflect.Constructor<?>> constructorCache = new ConcurrentHashMap<>();

//    @Around("@within(com.walter.datamaskingdemo.annotation.masking.Maskable)")
@Around("@annotation(com.walter.datamaskingdemo.annotation.masking.ApplyMasking)")
public Object maskFields(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof List) {
            maskListFields((List<?>) result);
        } else {
            result = maskObjectFields(result);
        }

        return result;
    }

    private void maskListFields(List<?> list) {
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            Object maskedItem = maskObjectFields(item);
            if (maskedItem != item) {
                // Replace with new masked instance
                try {
                    java.lang.reflect.Method setMethod = list.getClass().getMethod("set", int.class, Object.class);
                    setMethod.invoke(list, i, maskedItem);
                } catch (Exception e) {
                    // If list doesn't support set, continue
                }
            }
        }
    }

    public Object maskObjectFields(Object obj) {
        if (obj == null) return obj;

        Class<?> clazz = obj.getClass();

        // Check cache first
        Boolean hasMasked = hasMaskedFieldsCache.get(clazz);
        if (hasMasked == null) {
            hasMasked = hasMaskedFields(clazz);
            hasMaskedFieldsCache.put(clazz, hasMasked);
        }

        if (!hasMasked) {
            return obj;
        }

        Object maskedObj = obj;

        // Get cached fields
        Field[] fields = fieldCache.computeIfAbsent(clazz, k -> {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                    // Skip fields that cannot be made accessible (e.g., serialVersionUID)
                    System.err.println("Cannot make field accessible: " + field.getName() + " - " + e.getMessage());
                }
            }
            return declaredFields;
        });

        // Get current user context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication != null ? authentication.getName() : null;
        Set<String> currentUserRoles = authentication != null ? 
            authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
                .collect(Collectors.toSet()) : 
            Set.of();

        // Process fields
        for (Field field : fields) {
            if (field.isAnnotationPresent(Mask.class)) {
                try {
                    Object value = field.get(obj);

                    if (value instanceof String) {
                        Mask maskAnnotation = field.getAnnotation(Mask.class);
                        
                        // Check if masking should be applied
                        boolean shouldMask = shouldApplyMasking(obj, currentUsername, currentUserRoles, maskAnnotation);
                        
                        if (shouldMask) {
                            String maskedValue = maskString((String) value,
                                    maskAnnotation.prefix(),
                                    maskAnnotation.suffix(),
                                    maskAnnotation.maskChar());

                            // For records, create new instance
                            if (clazz.isRecord()) {
                                maskedObj = createMaskedRecordInstance(maskedObj, field.getName(), maskedValue);
                            } else {
                                field.set(obj, maskedValue);
                            }
                        }
                    }
                } catch (Exception e) {
                    // Log error but continue processing
                    System.err.println("Error masking field: " + field.getName() + " - " + e.getMessage());
                }
            }
        }

        return maskedObj;
    }

    private Object createMaskedRecordInstance(Object original, String fieldName, String maskedValue) {
        Class<?> clazz = original.getClass();

        // Get cached constructor
        java.lang.reflect.Constructor<?> constructor = constructorCache.computeIfAbsent(clazz, k -> {
            try {
                return k.getConstructors()[0];
            } catch (Exception e) {
                throw new RuntimeException("Cannot get constructor for " + k.getSimpleName(), e);
            }
        });

        try {
            // Get cached fields
            Field[] fields = fieldCache.get(clazz);
            Object[] fieldValues = new Object[fields.length];

            for (int i = 0; i < fields.length; i++) {
                Object value = fields[i].get(original);

                // Replace masked field
                if (fields[i].getName().equals(fieldName)) {
                    fieldValues[i] = maskedValue;
                } else {
                    // Check if this field is a nested object that needs masking
                    if (value != null) {
                        Class<?> nestedClass = value.getClass();
                        Boolean nestedHasMasked = hasMaskedFieldsCache.get(nestedClass);
                        if (nestedHasMasked == null) {
                            nestedHasMasked = hasMaskedFields(nestedClass);
                            hasMaskedFieldsCache.put(nestedClass, nestedHasMasked);
                        }

                        if (nestedHasMasked) {
                            fieldValues[i] = maskObjectFields(value);
                        } else {
                            fieldValues[i] = value;
                        }
                    } else {
                        fieldValues[i] = value;
                    }
                }
            }

            return constructor.newInstance(fieldValues);

        } catch (Exception e) {
            System.err.println("Cannot create masked record instance: " + e.getMessage());
            return original;
        }
    }

    private boolean hasMaskedFields(Class<?> clazz) {
        // Check cache first
        if (hasMaskedFieldsCache.containsKey(clazz)) {
            return hasMaskedFieldsCache.get(clazz);
        }

        Field[] fields = fieldCache.computeIfAbsent(clazz, k -> {
            Field[] declaredFields = k.getDeclaredFields();
            for (Field field : declaredFields) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                    // Skip fields that cannot be made accessible (e.g., serialVersionUID)
                    // Don't log here to avoid noise
                }
            }
            return declaredFields;
        });

        for (Field field : fields) {
            try {
                if (field.isAnnotationPresent(Mask.class)) {
                    return true;
                }
            } catch (Exception e) {
                // Skip fields that cannot be accessed
                continue;
            }
        }
        return false;
    }

    private boolean shouldApplyMasking(Object obj, String currentUsername, Set<String> currentUserRoles, Mask maskAnnotation) {
        // If no authentication context, apply masking
        if (currentUsername == null) {
            return true;
        }

        // Check if user is the owner of the record first (owners always see unmasked data)
        boolean isOwner = isRecordOwner(obj, currentUsername);
        if (isOwner) {
            return false;
        }

        // Get allowed roles
        String[] allowedRoles = maskAnnotation.allowedRoles();
        
        // If no allowed roles specified, always mask (except owners which we already checked)
        if (allowedRoles.length == 0) {
            return true;
        }

        // Check if current user has any of the allowed roles
        boolean hasAllowedRole = Arrays.stream(allowedRoles)
                .anyMatch(currentUserRoles::contains);
        
        // Only mask if user doesn't have allowed roles
        return !hasAllowedRole;
    }

    private boolean isRecordOwner(Object obj, String currentUsername) {
        try {
            // Check for 'id' field that matches the username
            Field idField = obj.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(obj);
            
            // Check for 'username' field
            try {
                Field usernameField = obj.getClass().getDeclaredField("username");
                usernameField.setAccessible(true);
                Object usernameValue = usernameField.get(obj);
                
                // Owner if current username matches the record's username or id
                return currentUsername.equals(usernameValue) || currentUsername.equals(idValue);
            } catch (NoSuchFieldException e) {
                // No username field, check if id matches username
                return currentUsername.equals(idValue);
            }
        } catch (Exception e) {
            // If we can't determine ownership, apply masking
            return false;
        }
    }

    private String maskString(String value, int prefix, int suffix, char maskChar) {
        if (value == null || value.length() <= prefix + suffix) {
            return value;
        }

        int maskLength = value.length() - prefix - suffix;
        StringBuilder masked = new StringBuilder(value.length());

        // Add prefix
        if (prefix > 0) {
            masked.append(value.substring(0, prefix));
        }

        // Add masked characters
        for (int i = 0; i < maskLength; i++) {
            masked.append(maskChar);
        }

        // Add suffix
        if (suffix > 0) {
            masked.append(value.substring(value.length() - suffix));
        }

        return masked.toString();
    }
}
