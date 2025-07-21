package com.example.fastapi.service;

import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dboModel.Users;
import com.example.fastapi.repository.PermissionsRepository;
import com.example.fastapi.repository.RolesRepository;
import com.example.fastapi.repository.UserPassRepository;
import com.example.fastapi.repository.UsersRepository;
import com.example.fastapi.repository.UserCustomRepositoryImpl;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.dto.PermissionDTO;
import com.example.fastapi.dto.RegisterDTO;
import com.example.fastapi.dto.UpdateUserDTO;
import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.service.DynamicMongoService;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;


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
    private UserCustomRepositoryImpl userCustomRepositoryImpl;
    private DynamicMongoService dynamicMongoService;


    private final MongoTemplate mongoTemplate;

    public UsersService(
            UsersRepository userRepository,
            UserPassRepository userPassRepository,
            PermissionsRepository permissionsRepository,
            RolesRepository rolesRepository,
            UserCustomRepositoryImpl userCustomRepositoryImpl,
            DynamicMongoService dynamicMongoServic,
            MongoTemplate mongoTemplate
    ) {
        this.userRepository = userRepository;
        this.userPassRepository = userPassRepository;
        this.permissionsRepository = permissionsRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userCustomRepositoryImpl = userCustomRepositoryImpl;
        this.dynamicMongoService = dynamicMongoServic;
        this.mongoTemplate = mongoTemplate;
    }

    public UserProfileDTO getUserProfileById(String userId) {
        return userCustomRepositoryImpl.findUserProfileByUserId(userId);
    }

    public boolean userLogin(String username, String password) {
        try {
            // mongoTemplate داینامیک از طریق spring به این کلاس تزریق شده فرض می‌کنیم
            Document userDoc = mongoTemplate.findOne(
                    Query.query(Criteria.where("username").is(username)),
                    Document.class,
                    "userPass"
            );

            if (userDoc == null) {
                return false;
            }

            String hashedPassword = userDoc.getString("password");

            if (passwordEncoder.matches(password, hashedPassword)) {
                return true;
            } else {
                return false;
            }
        } finally {
            // حذف ContextHolder حتماً در controller انجام شود و اینجا نیاز نیست
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

            // فقط ذخیره id ها در مدل Users
            user.setRoleId(dto.getRoleId());
            user.setPermissionId(dto.getPermissionId());

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
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return false;
        }
    }




    public Optional<Users> getByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public List<UserProfileDTO> getAllUsersWithUsernames() {
        List<Users> users = usersRepository.findAll();
        List<UserPass> userPasses = userPassRepository.findAll();

        // ساخت مپ userId به username
        Map<String, String> userIdToUsername = userPasses.stream()
                .collect(Collectors.toMap(UserPass::getUserId, UserPass::getUsername));

        List<UserProfileDTO> result = new ArrayList<>();

        for (Users user : users) {
            String username = userIdToUsername.getOrDefault(user.getId(), "Unknown Username");

            // واکشی رول
            RolesDTO roleDTO = null;
            if (user.getRoleId() != null) {
                Optional<Roles> roleOpt = rolesRepository.findById(new ObjectId(user.getRoleId()));
                roleDTO = roleOpt.map(role -> new RolesDTO(role.getId(), role.getRoleName()))
                        .orElse(new RolesDTO(null, "Unknown Role"));
            } else {
                roleDTO = new RolesDTO(null, "Unknown Role");
            }

            // واکشی پرمیشن
            PermissionDTO permissionDTO = null;
            if (user.getPermissionId() != null) {
                Optional<Permissions> permissionOpt = permissionsRepository.findById(new ObjectId(user.getPermissionId()));
                permissionDTO = permissionOpt.map(p -> new PermissionDTO(p.getId(), p.getPermissionName()))
                        .orElse(new PermissionDTO(null, "Unknown Permission"));
            } else {
                permissionDTO = new PermissionDTO(null, "Unknown Permission");
            }

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
        // 1. چک کردن ContextHolder
        String storeName = ContextHolder.getStoreName();
        if (storeName == null) {
            throw new IllegalStateException("Store name is not set in ContextHolder");
        }

        // 2. گرفتن UserPass document با MongoTemplate
        Document userPassDoc = mongoTemplate.getCollection("userPass")
                .find(new Document("username", username))
                .first();

        if (userPassDoc == null) {
            return null;
        }

        // 3. بررسی پسورد
        String hashedPassword = userPassDoc.getString("password");
        if (!passwordEncoder.matches(password, hashedPassword)) {
            return null;
        }

        // 4. گرفتن user_id از userPassDoc
        ObjectId userIdObj = userPassDoc.getObjectId("user_id");
        if (userIdObj == null) {
            return null;
        }
        String userId = userIdObj.toHexString();

        // 5. واکشی user document
        Document userDoc = mongoTemplate.getCollection("users")
                .find(new Document("_id", new ObjectId(userId)))
                .first();

        if (userDoc == null) {
            return null;
        }

        // 6. واکشی role
        RolesDTO roleDTO = new RolesDTO(null, "Unknown Role");
        ObjectId roleIdObj = userDoc.getObjectId("roleId");
        if (roleIdObj != null) {
            Document roleDoc = mongoTemplate.getCollection("roles")
                    .find(new Document("_id", roleIdObj))
                    .first();
            if (roleDoc != null) {
                roleDTO = new RolesDTO(roleDoc.getObjectId("_id").toHexString(), roleDoc.getString("roleName"));
            }
        }

        // 7. واکشی permission
        PermissionDTO permissionDTO = new PermissionDTO(null, "Unknown Permission");
        ObjectId permissionIdObj = userDoc.getObjectId("permissionId");
        if (permissionIdObj != null) {
            Document permissionDoc = mongoTemplate.getCollection("permissions")
                    .find(new Document("_id", permissionIdObj))
                    .first();
            if (permissionDoc != null) {
                permissionDTO = new PermissionDTO(permissionDoc.getObjectId("_id").toHexString(), permissionDoc.getString("permissionName"));
            }
        }

        // 8. ساخت و برگرداندن UserProfileDTO
        return new UserProfileDTO(
                userDoc.getObjectId("_id").toHexString(),
                username,
                userDoc.getString("firstName"),
                userDoc.getString("lastName"),
                roleDTO,
                permissionDTO
        );
    }


    public boolean updateUser(String userId, UpdateUserDTO dto) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            logger.warn("User not found with id: {}", userId);
            return false;
        }
        Users user = optionalUser.get();

        // Update basic info
        if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {
            user.setLastName(dto.getLastName());
        }

        // Update roleId if provided
        if (dto.getRoleId() != null && !dto.getRoleId().isEmpty()) {
            // Optional: بررسی وجود رول در دیتابیس (می‌تونی اضافه کنی)
            user.setRoleId(dto.getRoleId());
        }

        // Update permissionId if provided
        if (dto.getPermissionId() != null && !dto.getPermissionId().isEmpty()) {
            // Optional: بررسی وجود permission در دیتابیس (می‌تونی اضافه کنی)
            user.setPermissionId(dto.getPermissionId());
        }

        // ذخیره اطلاعات اصلاح شده
        userRepository.save(user);

        // Update UserPass (username and optionally password)
        Optional<UserPass> optionalUserPass = userPassRepository.findByUserId(new ObjectId(userId));
        if (!optionalUserPass.isPresent()) {
            logger.warn("UserPass not found for userId: {}", userId);
            return false;
        }
        UserPass userPass = optionalUserPass.get();

        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            userPass.setUsername(dto.getUsername());
        }

        if (dto.isUpdatePassword() && dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            userPass.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userPassRepository.save(userPass);

        return true;
    }


    public boolean deleteUserById(String userId) {
        try {
            Optional<Users> user = usersRepository.findById(userId);
            if (!user.isPresent()) {
                return false;
            }

            Optional<UserPass> userPass = userPassRepository.findByUserId(new ObjectId(userId));

            usersRepository.deleteById(userId);

            userPass.ifPresent(up -> userPassRepository.deleteById(up.getId()));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
