package com.example.fastapi.service;

import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dboModel.Users;
import com.example.fastapi.repository.PermissionsRepository;
import com.example.fastapi.repository.RolesRepository;
import com.example.fastapi.repository.UserPassRepository;
import com.example.fastapi.repository.UsersRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository userRepository;
    private final UserPassRepository userPassRepository;
    private final PermissionsRepository permissionsRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsersService(
            UsersRepository userRepository,
            UserPassRepository userPassRepository,
            PermissionsRepository permissionsRepository,
            RolesRepository rolesRepository
    ) {
        this.userRepository = userRepository;
        this.userPassRepository = userPassRepository;
        this.permissionsRepository = permissionsRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean userLogin(String username, String password) {
        Optional<UserPass> obj = userPassRepository.findByUsername(username);
        if (obj.isPresent()) {
            String hashedPassword = obj.get().getPassword();
            if (passwordEncoder.matches(password, hashedPassword)) {
                return true;
            } else {
                logger.info("Password mismatch for user: {}", username);
                return false;
            }
        } else {
            logger.info("User not found: {}", username);
            return false;
        }
    }



    public Optional<Users> getByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public UserProfileDTO getUserProfile(String username, String password) {
        Optional<UserPass> userPassOpt = userPassRepository.findByUsername(username);
        if (userPassOpt.isEmpty()) {
            return null;
        }

        UserPass userPass = userPassOpt.get();
        Optional<Users> userOpt = userRepository.findById(userPass.getUserId());
        if (userOpt.isEmpty()) {
            return null;
        }

        Users user = userOpt.get();

        Optional<Roles> roleOpt = rolesRepository.findById(user.getRoleId());
        String roleName = roleOpt.map(Roles::getRoleName).orElse("Unknown");

        Optional<Permissions> permOpt = permissionsRepository.findById(user.getPermissionId());
        String permissionName = permOpt.map(Permissions::getPermissionName).orElse("Unknown");

        return new UserProfileDTO(
                username,
                user.getFirstName(),
                user.getLastName(),
                roleName,
                permissionName
        );
    }
    public class UserProfileDTO {
        private String username;
        private String firstName;
        private String lastName;
        private String roleName;
        private String permissionName;

//        public UserProfileDTO() {}

        public UserProfileDTO(String username, String firstName, String lastName, String roleName, String permissionName) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.roleName = roleName;
            this.permissionName = permissionName;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }

        public String getPermissionName() { return permissionName; }
        public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    }
}
