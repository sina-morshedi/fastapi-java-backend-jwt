package com.example.fastapi.repository;
import java.util.Optional;

import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dboModel.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;


@Repository
public interface UserPassRepository extends MongoRepository<UserPass, String> {
    Optional<UserPass> findByUsername(String username);

    Optional<UserPass> findById(String _id);

    Optional<UserPass> findByUserId(ObjectId userId);

}