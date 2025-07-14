package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {

    Optional<Customer> findByFullName(String fullName);

    Optional<Customer> findByPhone(String phone);

    List<Customer> findByFullNameContainingIgnoreCase(String name);
}
