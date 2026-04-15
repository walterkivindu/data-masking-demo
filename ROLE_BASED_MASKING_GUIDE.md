# Role-Based Data Masking Guide

## Overview

The enhanced masking service now supports role-based access control with session context. This allows different users to see different levels of data detail based on their roles and ownership of records.

## How It Works

### 1. Role-Based Access Control

The `@Mask` annotation now supports an `allowedRoles` parameter:

```java
@Mask(prefix = 2, suffix = 4, maskChar = '*', allowedRoles = {"ADMIN", "MANAGER"}) String email
```

**Behavior:**
- Users with any of the specified roles can see the **unmasked** data
- Users without the specified roles see **masked** data
- Record owners can always see their **unmasked** data

### 2. Ownership Detection

The system automatically detects record ownership by checking:
- If the current username matches the record's `username` field
- If the current username matches the record's `id` field

### 3. Session Context

The masking system uses Spring Security context to determine:
- Current user's username
- Current user's roles/authorities

## Current Configuration Examples

### User Record Configuration

```java
@Maskable
public record User(
    String id, 
    String name, 
    String username, 
    @Mask(prefix = 2, suffix = 4, maskChar = '*', allowedRoles = {"ADMIN"}) String email, 
    Address address, 
    @Mask(prefix = 3, suffix = 4, maskChar = '*', allowedRoles = {"HR"}) String phone, 
    String website, 
    Company company
) {
    @Maskable
    public record Address(
        String street, 
        String suite, 
        String city, 
        @Mask(prefix = 0, suffix = 1, maskChar = '*', allowedRoles = {"MANAGER"}) String zipcode, 
        Geo geo
    ) {
        @Maskable
        public record Geo(String lat,  @Mask(prefix = 3, suffix = 3, maskChar = '-', allowedRoles = {})String lng) {}
    }
    
    public record Company(String name, String catchPhrase, String bs) {}
}
```

## Access Patterns

### ADMIN Role
- **Email**: Visible (ADMIN only allowed)
- **Phone**: Masked (HR only allowed, ADMIN not included)
- **Zipcode**: Masked (MANAGER only allowed, ADMIN not included)
- **Longitude**: Masked (no roles allowed, always masked)

### MANAGER Role
- **Email**: Masked (ADMIN only allowed, MANAGER not included)
- **Phone**: Masked (HR only allowed, MANAGER not included)
- **Zipcode**: Visible (MANAGER only allowed)
- **Longitude**: Masked (no roles allowed, always masked)

### HR Role
- **Email**: Masked (ADMIN only allowed, HR not included)
- **Phone**: Visible (HR only allowed)
- **Zipcode**: Masked (MANAGER only allowed, HR not included)
- **Longitude**: Masked (no roles allowed, always masked)

### Regular USER Role (No special permissions)
- **Email**: Masked
- **Phone**: Masked
- **Zipcode**: Masked
- **Longitude**: Masked

### Record Owner
Regardless of role, users can always see their own unmasked data.

## Your Current Situation

Based on your user data:
```json
{
  "username": "kiivindu",
  "role": "ADMIN"
}
```

**You now see selective masked data:**
1. You have the "ADMIN" role
2. Email is visible (ADMIN only allowed)
3. Phone, zipcode, and longitude are masked (ADMIN not included in allowed roles)
4. This gives you controlled access based on field-specific permissions

## Customization Options

### Option 1: Add ADMIN to More Fields

If you want ADMIN users to see more unmasked data:

```java
@Mask(prefix = 2, suffix = 4, maskChar = '*', allowedRoles = {"ADMIN", "MANAGER"}) String email
@Mask(prefix = 3, suffix = 4, maskChar = '*', allowedRoles = {"ADMIN", "HR"}) String phone
```

### Option 2: Create More Granular Roles

```java
@Mask(prefix = 2, suffix = 4, maskChar = '*', allowedRoles = {"DATA_VIEWER", "ADMIN"}) String email
@Mask(prefix = 3, suffix = 4, maskChar = '*', allowedRoles = {"FINANCE", "ADMIN"}) String phone
```

### Option 3: No Roles (Always Mask)

```java
@Mask(prefix = 2, suffix = 4, maskChar = '*') String email
```

When `allowedRoles` is empty, the field is always masked except for record owners.

## Testing the Implementation

Run the test suite to verify behavior:

```bash
./gradlew test --tests SimpleMaskingTest
```

This will demonstrate:
- ADMIN users see unmasked data
- Regular users see masked data
- Ownership detection works correctly

## Integration with Your Application

The masking is automatically applied when:
1. Controller methods are annotated with `@ApplyMasking`
2. The returned objects contain fields annotated with `@Mask`
3. Spring Security context is available

### Example Controller Usage

```java
@RestController
public class UsersController {
    
    @ApplyMasking
    @GetMapping("/users")
    public List<User> getAllUsers() {
        // Data will be masked based on current user's roles
        return usersService.getAllUsers();
    }
    
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {
        // No masking applied (no @ApplyMasking annotation)
        return usersService.getUserById(id);
    }
}
```

## Security Considerations

1. **Always mask by default**: Fields without `allowedRoles` are always masked
2. **Ownership override**: Record owners can always see their data
3. **Role hierarchy**: Consider implementing role hierarchy if needed
4. **Audit logging**: Consider logging access to sensitive unmasked data

## Troubleshooting

### Issue: "Data is not being masked"
- Check if user has ADMIN role
- Verify `allowedRoles` configuration
- Ensure `@ApplyMasking` annotation is present on controller method

### Issue: "Owners can't see their data"
- Check ownership detection logic
- Verify username/id matching
- Ensure Spring Security context is properly set

### Issue: "Role-based access not working"
- Verify role names match exactly (case-sensitive)
- Check Spring Security authority format (ROLE_ prefix)
- Ensure authentication is properly configured
