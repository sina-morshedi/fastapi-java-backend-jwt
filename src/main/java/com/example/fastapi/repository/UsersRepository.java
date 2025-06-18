package com.example.fastapi.repository;
import com.example.fastapi.dboModel.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<Users, String> {

    Optional<Users> findByFirstName(String firstName);
    Optional<Users> findById(String _id);
}
