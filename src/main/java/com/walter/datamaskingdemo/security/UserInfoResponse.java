package com.walter.datamaskingdemo.security;

public record UserInfoResponse(
    int code,
    UserData data,
    String message,
    long timeStamp
) {
}
