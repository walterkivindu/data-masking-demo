package com.walter.datamaskingdemo;

import com.walter.datamaskingdemo.api.User;
import com.walter.datamaskingdemo.masking.MaskingAspect;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleIntegrationTest {

    @Test
    void testRegularUserSeesMaskedData() {
        // Set up authentication with USER role (no special access)
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "regularuser", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        MaskingAspect maskingAspect = new MaskingAspect();
        
        // Create test user data
        User user = new User("1", "Leanne Graham", "Bret", "Sincere@april.biz", 
                           new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", 
                                          new User.Address.Geo("-37.3159", "81.1496")),
                           "1-770-736-8031 x56442", "hildegard.org", 
                           new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        // Apply masking directly
        Object result = maskingAspect.maskObjectFields(user);
        
        System.out.println("=== REGULAR USER TEST ===");
        System.out.println("Original email: " + user.email());
        System.out.println("Masked email: " + ((User) result).email());
        System.out.println("Original phone: " + user.phone());
        System.out.println("Masked phone: " + ((User) result).phone());
        
        // Verify masking is applied
        assertInstanceOf(User.class, result);
        User maskedUser = (User) result;
        assertNotEquals("Sincere@april.biz", maskedUser.email(), "Email should be masked for regular user");
        assertNotEquals("1-770-736-8031 x56442", maskedUser.phone(), "Phone should be masked for regular user");
        
        System.out.println("Regular user test PASSED - masking is working!");
    }
    
    @Test
    void testAdminSeesUnmaskedData() {
        // Set up authentication with ADMIN role
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "kiivindu", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        MaskingAspect maskingAspect = new MaskingAspect();
        
        // Create test user data
        User user = new User("1", "Leanne Graham", "Bret", "Sincere@april.biz", 
                           new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", 
                                          new User.Address.Geo("-37.3159", "81.1496")),
                           "1-770-736-8031 x56442", "hildegard.org", 
                           new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        // Apply masking directly
        Object result = maskingAspect.maskObjectFields(user);
        
        System.out.println("=== ADMIN USER TEST ===");
        System.out.println("Original email: " + user.email());
        System.out.println("Result email: " + ((User) result).email());
        System.out.println("Original phone: " + user.phone());
        System.out.println("Result phone: " + ((User) result).phone());
        
        // Verify masking is selectively applied for admin
        assertInstanceOf(User.class, result);
        User maskedUser = (User) result;
        assertEquals("Sincere@april.biz", maskedUser.email(), "ADMIN should see unmasked email");
        assertNotEquals("1-770-736-8031 x56442", maskedUser.phone(), "ADMIN should see masked phone");
        
        System.out.println("Admin user test PASSED - admin sees selective data!");
    }
    
    @Test
    void testOwnerSeesUnmaskedData() {
        // Set up authentication as the record owner
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "Bret", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        MaskingAspect maskingAspect = new MaskingAspect();
        
        // Create test user data where username matches auth
        User user = new User("1", "Leanne Graham", "Bret", "Sincere@april.biz", 
                           new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", 
                                          new User.Address.Geo("-37.3159", "81.1496")),
                           "1-770-736-8031 x56442", "hildegard.org", 
                           new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        // Apply masking directly
        Object result = maskingAspect.maskObjectFields(user);
        
        System.out.println("=== OWNER TEST ===");
        System.out.println("Current user: Bret");
        System.out.println("Record username: " + user.username());
        System.out.println("Original email: " + user.email());
        System.out.println("Result email: " + ((User) result).email());
        
        // Verify masking is NOT applied for owner
        assertInstanceOf(User.class, result);
        User maskedUser = (User) result;
        assertEquals("Sincere@april.biz", maskedUser.email(), "Owner should see unmasked email");
        assertEquals("1-770-736-8031 x56442", maskedUser.phone(), "Owner should see unmasked phone");
        
        System.out.println("Owner test PASSED - owner sees unmasked data!");
    }
}
