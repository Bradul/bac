package org.example.cs_service;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CSProblemRepository extends MongoRepository<CSProblem, Integer> {

}
