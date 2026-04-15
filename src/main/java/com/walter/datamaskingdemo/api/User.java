package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.annotation.masking.Mask;
import com.walter.datamaskingdemo.annotation.masking.Maskable;

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
