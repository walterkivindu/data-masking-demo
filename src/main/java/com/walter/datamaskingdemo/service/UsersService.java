package com.walter.datamaskingdemo.service;

import com.walter.datamaskingdemo.api.User;
import com.walter.datamaskingdemo.client.UsersClient;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Service
public class UsersService {

    private final UsersClient usersClient;

    public UsersService(HttpServiceProxyFactory factory) {
        this.usersClient = factory.createClient(UsersClient.class);
    }

    public List<User> getAllUsers() {
        return usersClient.getAllUsers();
    }

    public User getUserById(String id) {
        return usersClient.getUserById(id);
    }
}
