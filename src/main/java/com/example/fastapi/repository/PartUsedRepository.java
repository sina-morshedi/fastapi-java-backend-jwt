package com.example.fastapi.repository;

import com.example.fastapi.dboModel.PartUsed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartUsedRepository extends MongoRepository<PartUsed, String> {

}
