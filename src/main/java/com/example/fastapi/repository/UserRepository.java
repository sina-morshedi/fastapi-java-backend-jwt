package com.example.fastapi.repository;
import com.example.fastapi.dboModel.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {

    List<Users> findByFirstName(String firstName);
}
