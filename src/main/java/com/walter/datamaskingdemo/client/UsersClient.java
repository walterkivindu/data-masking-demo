package com.walter.datamaskingdemo.client;

import com.walter.datamaskingdemo.api.User;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface UsersClient {

    @GetExchange("/users")
    List<User> getAllUsers();

    @GetExchange("/users/{id}")
    User getUserById(String id);
}
