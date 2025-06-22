package com.example.fastapi.service;

import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dboModel.Users;
import com.example.fastapi.repository.PermissionsRepository;
import com.example.fastapi.repository.RolesRepository;
import com.example.fastapi.repository.UserPassRepository;
import com.example.fastapi.repository.UsersRepository;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.RoleDTO;
import com.example.fastapi.dto.PermissionDTO;
import com.example.fastapi.dto.RegisterDTO;


import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;




@Service
public class UsersService {
    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository userRepository;
    private final UserPassRepository userPassRepository;
    private final PermissionsRepository permissionsRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;


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

    public boolean usernameExists(String username) {
        return userPassRepository.findByUsername(username).isPresent();
    }

    public boolean registerUser(RegisterDTO dto) {
        Optional<UserPass> existingUser = userPassRepository.findByUsername(dto.getUsername());
        if (existingUser.isPresent()) {
            return false;
        }

        try {
            Users user = new Users();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setRoleId(new ObjectId(dto.getRoleId()));
            user.setPermissionId(new ObjectId(dto.getPermissionId()));

            Users savedUser = usersRepository.save(user);
            if (savedUser == null || savedUser.getId() == null) {
                return false;
            }

            UserPass userPass = new UserPass();
            userPass.setUsername(dto.getUsername());
            userPass.setPassword(passwordEncoder.encode(dto.getPassword()));
            userPass.setUserId(savedUser.getId());

            UserPass savedUserPass = userPassRepository.save(userPass);
            if (savedUserPass == null || savedUserPass.getId() == null) {
                // می‌تونی اینجا کار rollback بزنی یا لاگ خطا بگیری
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();  // یا لاگ به صورت بهتر
            return false;
        }
    }


    public Optional<Users> getByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public List<UserProfileDTO> getAllUsersWithUsernames() {
        List<Users> users = usersRepository.findAll();
        List<UserPass> userPasses = userPassRepository.findAll();
        List<Roles> rolesList = rolesRepository.findAll();
        List<Permissions> permissionsList = permissionsRepository.findAll();

        Map<String, String> userIdToUsername = userPasses.stream()
                .collect(Collectors.toMap(UserPass::getUserId, UserPass::getUsername));

        Map<String, Roles> roleIdToObject = rolesList.stream()
                .collect(Collectors.toMap(Roles::getId, role -> role));

        Map<String, Permissions> permissionIdToObject = permissionsList.stream()
                .collect(Collectors.toMap(Permissions::getId, perm -> perm));

        List<UserProfileDTO> result = new ArrayList<>();

        for (Users user : users) {
            String username = userIdToUsername.getOrDefault(user.getId(), "Unknown Username");

            String roleId = user.getRoleId() != null ? user.getRoleId().toHexString() : null;
            String permissionId = user.getPermissionId() != null ? user.getPermissionId().toHexString() : null;

            RoleDTO roleDTO = new RoleDTO(
                    roleId,
                    roleIdToObject.containsKey(roleId) ? roleIdToObject.get(roleId).getRoleName() : "Unknown Role"
            );

            PermissionDTO permissionDTO = new PermissionDTO(
                    permissionId,
                    permissionIdToObject.containsKey(permissionId) ? permissionIdToObject.get(permissionId).getPermissionName() : "Unknown Permission"
            );

            UserProfileDTO dto = new UserProfileDTO(
                    user.getId(),
                    username,
                    user.getFirstName(),
                    user.getLastName(),
                    roleDTO,
                    permissionDTO
            );

            result.add(dto);
        }

        return result;
    }


    public long countAllUser() {
        return userRepository.count();
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

        // Get role and build RoleDTO
        RoleDTO roleDTO;
        if (user.getRoleId() != null) {
            Optional<Roles> roleOpt = rolesRepository.findById(user.getRoleId().toHexString());
            if (roleOpt.isPresent()) {
                Roles role = roleOpt.get();
                roleDTO = new RoleDTO(role.getId(), role.getRoleName());
            } else {
                roleDTO = new RoleDTO(null, "Unknown");
            }
        } else {
            roleDTO = new RoleDTO(null, "Unknown");
        }

        // Get permission and build PermissionDTO
        PermissionDTO permissionDTO;
        if (user.getPermissionId() != null) {
            Optional<Permissions> permOpt = permissionsRepository.findById(user.getPermissionId().toHexString());
            if (permOpt.isPresent()) {
                Permissions perm = permOpt.get();
                permissionDTO = new PermissionDTO(perm.getId(), perm.getPermissionName());
            } else {
                permissionDTO = new PermissionDTO(null, "Unknown");
            }
        } else {
            permissionDTO = new PermissionDTO(null, "Unknown");
        }

        return new UserProfileDTO(
                user.getId(),
                username,
                user.getFirstName(),
                user.getLastName(),
                roleDTO,
                permissionDTO
        );
    }

}
