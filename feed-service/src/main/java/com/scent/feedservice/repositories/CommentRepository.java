package com.scent.feedservice.repositories;


import com.scent.feedservice.data.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CommentRepository extends ReactiveMongoRepository<Employee, String> {
}
