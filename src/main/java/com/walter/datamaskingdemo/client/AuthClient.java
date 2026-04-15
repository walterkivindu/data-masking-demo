package com.walter.datamaskingdemo.client;

import com.walter.datamaskingdemo.security.UserInfoResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

public interface AuthClient {
    @GetExchange("/api/v1/auth/userinfo")
    UserInfoResponse getUserInfo(@RequestHeader("Authorization") String authorization);
}
