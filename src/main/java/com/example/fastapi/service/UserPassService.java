package com.example.fastapi.service;
import com.example.fastapi.dboModel.UserPass;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.example.fastapi.repository.UserPassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Service
public class UserPassService {
    private static final Logger logger = LoggerFactory.getLogger(UserPassService.class);

    private final UserPassRepository userPassRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserPassService(UserPassRepository userPassRepository) {
        this.userPassRepository = userPassRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<UserPass> getByUsername(String username) {
        Optional<UserPass> userPass = userPassRepository.findByUsername(username);

        return userPass;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void registerUser(String username, String rawPassword) {
        String hashedPassword = encodePassword(rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
