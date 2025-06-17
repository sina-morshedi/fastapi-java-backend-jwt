package com.example.fastapi.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.fastapi.service.UserService;
import com.example.fastapi.dboModel.Users;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public List<Users> getByName(@RequestParam String firstName) {
        logger.debug("Received GET /users/ request with firstName = {}", firstName);
        List<Users> users = userService.getByFirstName(firstName);
        logger.debug("Found {} users for firstName = {}", users.size(), firstName);
        return users;
    }
}
