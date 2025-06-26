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
import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.dto.PermissionDTO;
import com.example.fastapi.dto.RegisterDTO;


import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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

            // ساخت آبجکت Roles و Permissions با داده‌های dto
            Roles role = new Roles(new ObjectId(dto.getRoleId()), dto.getRoleName());
            Permissions permission = new Permissions(new ObjectId(dto.getPermissionId()), dto.getPermissionName());


            user.setRole(role);
            user.setPermission(permission);

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
                // اگر میخوای rollback بزنی یا لاگ بهتر بزنی اینجا اضافه کن
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();  // بهتره لاگ بهتری بزنی یا لاگ‌ها رو مدیریت کنی
            return false;
        }
    }



    public Optional<Users> getByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public List<UserProfileDTO> getAllUsersWithUsernames() {
        List<Users> users = usersRepository.findAll();
        List<UserPass> userPasses = userPassRepository.findAll();

        Map<String, String> userIdToUsername = userPasses.stream()
                .collect(Collectors.toMap(UserPass::getUserId, UserPass::getUsername));

        List<UserProfileDTO> result = new ArrayList<>();

        for (Users user : users) {
            String username = userIdToUsername.getOrDefault(user.getId(), "Unknown Username");

            Roles role = user.getRole();
            Permissions permission = user.getPermission();

            String roleId = role != null ? role.getId() : null;
            String roleName = (role != null && role.getRoleName() != null) ? role.getRoleName() : "Unknown Role";

            String permissionId = permission != null ? permission.getId() : null;
            String permissionName = (permission != null && permission.getPermissionName() != null) ? permission.getPermissionName() : "Unknown Permission";

            RolesDTO roleDTO = new RolesDTO(roleId, roleName);
            PermissionDTO permissionDTO = new PermissionDTO(permissionId, permissionName);

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

        // اگر لازم باشه رمز را اینجا هم بررسی کنیم (بسته به منطق برنامه)
        // اگر چک رمز لازم است، اینجا باید passwordEncoder.matches(password, userPass.getPassword()) بررسی شود.

        Optional<Users> userOpt = userRepository.findById(userPass.getUserId());
        if (userOpt.isEmpty()) {
            return null;
        }

        Users user = userOpt.get();

        // مستقیماً از آبجکت های role و permission استفاده می‌کنیم
        Roles role = user.getRole();
        Permissions permission = user.getPermission();

        RolesDTO roleDTO = (role != null)
                ? new RolesDTO(role.getId(), role.getRoleName())
                : new RolesDTO(null, "Unknown");

        PermissionDTO permissionDTO = (permission != null)
                ? new PermissionDTO(permission.getId(), permission.getPermissionName())
                : new PermissionDTO(null, "Unknown");

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
