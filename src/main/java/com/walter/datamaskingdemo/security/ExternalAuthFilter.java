package com.walter.datamaskingdemo.security;

import com.walter.datamaskingdemo.client.AuthClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class ExternalAuthFilter extends OncePerRequestFilter {
    
    private final AuthClient authClient;
    
    public ExternalAuthFilter(AuthClient authClient) {
        this.authClient = authClient;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && !authorizationHeader.isBlank()) {
            try {
                UserInfoResponse userInfoResponse = authClient.getUserInfo(authorizationHeader);
                
                if (userInfoResponse.code() == 200 && userInfoResponse.data() != null) {
                    UserData userData = userInfoResponse.data();
                    
                    // Use role as the authority, fallback to userType if role is null
                    String authority = userData.role() != null ? userData.role() : userData.userType();
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + authority));
                    
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userData.username(),
                            null,
                            authorities
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Log error but continue chain - authentication will fail if needed
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
