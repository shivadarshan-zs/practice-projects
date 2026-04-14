package com.example.RedisImplimentation.controllers;

import com.example.RedisImplimentation.models.User;
import com.example.RedisImplimentation.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("user")
    public List<User> getAllUsers(){
        log.info("GET request received for all users");
        List<User> users = userService.getUsers();
        log.debug("Retrieved {} users", users.size());
        return users;
    }

    @GetMapping("user/{id}")
    public Optional<User> getUserByID(@PathVariable int id){
        log.info("GET request received for user id: {}", id);
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()) {
            log.info("User id: {} returned to client", id);
        } else {
            log.warn("User id: {} not found", id);
        }
        return user;
    }

    @PostMapping("user")
    public User addUser(@RequestBody User u){
        log.info("POST request received to add new user");
        User savedUser = userService.addUser(u);
        log.info("User created with id: {}", savedUser.getId());
        return savedUser;
    }
}
