package com.walter.datamaskingdemo;

import com.walter.datamaskingdemo.api.User;
import com.walter.datamaskingdemo.masking.MaskingAspect;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class AdminRoleTest {

    @Test
    void testAdminSeesAllData() {
        // Set up authentication with ADMIN role (like your user)
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "kiivindu", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        MaskingAspect maskingAspect = new MaskingAspect();
        
        // Create test users similar to your data
        User user1 = new User("1", "Leanne Graham", "Bret", "Sincere@april.biz", 
                           new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", 
                                          new User.Address.Geo("-37.3159", "81.1496")),
                           "1-770-736-8031 x56442", "hildegard.org", 
                           new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        User user2 = new User("2", "Ervin Howell", "Antonette", "Shanna@melissa.tv", 
                           new User.Address("Victor Plains", "Suite 879", "Wisokyburgh", "90566-7771", 
                                          new User.Address.Geo("-43.9509", "-34.4618")),
                           "010-692-6593 x09125", "anastasia.net", 
                           new User.Company("Deckow-Crist", "Proactive didactic contingency", "synergize scalable supply-chains"));

        // Apply masking to both users
        Object result1 = maskingAspect.maskObjectFields(user1);
        Object result2 = maskingAspect.maskObjectFields(user2);

        System.out.println("=== ADMIN USER (kiivindu) ===");
        System.out.println("Current user roles: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println();
        
        System.out.println("User 1 (Bret) - ADMIN should see all data:");
        System.out.println("Original: " + user1);
        System.out.println("Masked:   " + result1);
        System.out.println();
        
        System.out.println("User 2 (Antonette) - ADMIN should see all data:");
        System.out.println("Original: " + user2);
        System.out.println("Masked:   " + result2);
        System.out.println();
        
        // Verify that admin sees all unmasked data
        if (result1 instanceof User && result2 instanceof User) {
            User maskedUser1 = (User) result1;
            User maskedUser2 = (User) result2;
            
            System.out.println("=== VERIFICATION ===");
            System.out.println("User 1 email visible: " + maskedUser1.email());
            System.out.println("User 1 phone masked: " + maskedUser1.phone());
            System.out.println("User 1 zipcode masked: " + maskedUser1.address().zipcode());
            System.out.println("User 1 longitude masked: " + maskedUser1.address().geo().lng());
            System.out.println();
            System.out.println("User 2 email visible: " + maskedUser2.email());
            System.out.println("User 2 phone masked: " + maskedUser2.phone());
            System.out.println("User 2 zipcode masked: " + maskedUser2.address().zipcode());
            System.out.println("User 2 longitude masked: " + maskedUser2.address().geo().lng());
        }
    }

    @Test
    void testUserRoleSeesMaskedData() {
        // Clear authentication first
        SecurityContextHolder.clearContext();
        
        // Set up authentication with USER role (no special access)
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "regularuser", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        MaskingAspect maskingAspect = new MaskingAspect();
        
        User user = new User("1", "Leanne Graham", "Bret", "Sincere@april.biz", 
                           new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", 
                                          new User.Address.Geo("-37.3159", "81.1496")),
                           "1-770-736-8031 x56442", "hildegard.org", 
                           new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        // Apply masking
        Object result = maskingAspect.maskObjectFields(user);

        System.out.println("=== REGULAR USER (regularuser) ===");
        System.out.println("Current user roles: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println();
        
        System.out.println("Original: " + user);
        System.out.println("Masked:   " + result);
        System.out.println();
        
        if (result instanceof User) {
            User maskedUser = (User) result;
            System.out.println("=== VERIFICATION ===");
            System.out.println("Email should be masked: " + maskedUser.email());
            System.out.println("Phone should be masked: " + maskedUser.phone());
            System.out.println("Zipcode should be masked: " + maskedUser.address().zipcode());
            System.out.println("Longitude should be masked: " + maskedUser.address().geo().lng());
        }
    }
}
