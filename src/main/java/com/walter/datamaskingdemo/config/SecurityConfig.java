package com.walter.datamaskingdemo.config;

import com.walter.datamaskingdemo.client.AuthClient;
import com.walter.datamaskingdemo.security.ExternalAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Value("${external.auth.api.base-url}")
    private String externalAuthApiBaseUrl;
    
    @Bean
    public RestClient authRestClient() {
        return RestClient.builder()
                .baseUrl(externalAuthApiBaseUrl)
                .build();
    }
    
    @Bean
    public AuthClient authClient(RestClient authRestClient) {
        return org.springframework.web.service.invoker.HttpServiceProxyFactory.builder()
                .exchangeAdapter(org.springframework.web.client.support.RestClientAdapter.create(authRestClient))
                .build()
                .createClient(AuthClient.class);
    }
    
    @Bean
    public ExternalAuthFilter externalAuthFilter(AuthClient authClient) {
        return new ExternalAuthFilter(authClient);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ExternalAuthFilter externalAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll() // Add public endpoints if needed
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(externalAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
