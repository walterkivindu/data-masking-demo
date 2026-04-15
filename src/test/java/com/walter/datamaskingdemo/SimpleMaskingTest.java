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

class SimpleMaskingTest {

    @Test
    void testAdminSeesUnmaskedData() {
        // Set up authentication with ADMIN role (like your user)
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "kiivindu", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        MaskingAspect maskingAspect = new MaskingAspect();
        User user = new User("1", "Leanne Graham", "Bret", "Sincere@april.biz", 
                           new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", 
                                          new User.Address.Geo("-37.3159", "81.1496")),
                           "1-770-736-8031 x56442", "hildegard.org", 
                           new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        // Apply masking
        Object result = maskingAspect.maskObjectFields(user);

        // Admin should see selective unmasked data
        assertInstanceOf(User.class, result);
        User maskedUser = (User) result;
        
        // Only email should NOT be masked for ADMIN
        assertEquals("Sincere@april.biz", maskedUser.email(), "ADMIN should see unmasked email");
        assertNotEquals("1-770-736-8031 x56442", maskedUser.phone(), "ADMIN should see masked phone");
        assertNotEquals("92998-3874", maskedUser.address().zipcode(), "ADMIN should see masked zipcode");
        assertNotEquals("81.1496", maskedUser.address().geo().lng(), "ADMIN should see masked longitude");
        
        System.out.println("ADMIN sees selective data: PASSED");
    }

    @Test
    void testRegularUserSeesMaskedData() {
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

        // Regular user should see masked data
        assertInstanceOf(User.class, result);
        User maskedUser = (User) result;
        
        // These should be masked for regular USER
        assertNotEquals("Sincere@april.biz", maskedUser.email(), "Regular user should see masked email");
        assertNotEquals("1-770-736-8031 x56442", maskedUser.phone(), "Regular user should see masked phone");
        assertNotEquals("92998-3874", maskedUser.address().zipcode(), "Regular user should see masked zipcode");
        assertNotEquals("81.1496", maskedUser.address().geo().lng(), "Regular user should see masked longitude");
        
        System.out.println("Regular user sees masked data: PASSED");
        System.out.println("Masked email: " + maskedUser.email());
        System.out.println("Masked phone: " + maskedUser.phone());
        System.out.println("Masked zipcode: " + maskedUser.address().zipcode());
        System.out.println("Masked longitude: " + maskedUser.address().geo().lng());
    }
}
