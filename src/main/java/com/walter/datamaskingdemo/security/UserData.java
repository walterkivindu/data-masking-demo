package com.walter.datamaskingdemo.security;

import java.util.UUID;

public record UserData(
    String email,
    String firstName,
    UUID id,
    String idType,
    String identityNumber,
    String phoneNumber,
    String role,
    String surname,
    String userType,
    String username,
    Integer yearOfBirth
) {
}
