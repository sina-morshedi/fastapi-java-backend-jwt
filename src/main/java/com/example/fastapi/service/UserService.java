package com.example.fastapi.service;

import com.example.fastapi.dboModel.Users;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.fastapi.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> getByFirstName(String firstName) {
        logger.debug("UserService.getByFirstName called with firstName = {}", firstName);
        List<Users> users = userRepository.findByFirstName(firstName);
        logger.debug("UserService.getByFirstName returning {} users", users.size());
        return users;
    }
}
