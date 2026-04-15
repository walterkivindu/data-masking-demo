package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.annotation.masking.ApplyMasking;
import com.walter.datamaskingdemo.annotation.masking.Maskable;
import com.walter.datamaskingdemo.service.UsersService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
//@Maskable
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    @ApplyMasking
    public List<User> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return usersService.getUserById(id);
    }
}
