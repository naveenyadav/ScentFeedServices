package com.scent.feedservices.controller;


import com.scent.feedservices.data.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee,String> {

}
