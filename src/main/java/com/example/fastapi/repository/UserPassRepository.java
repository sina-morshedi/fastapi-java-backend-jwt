package com.example.fastapi.repository;
import java.util.Optional;

import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dboModel.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public interface UserPassRepository extends MongoRepository<UserPass, Long> {
    Optional<UserPass> findByUsername(String username);

    Optional<Users> findById(String _id);
}