package com.scent.feedservice.controller;

import com.scent.feedservice.data.Employee;
import com.scent.feedservice.repositories.CommentRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * This controller class file is used to handle following:
 * Purpose:
 * Methods:
 *
 * @author nyadav
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    private CommentRepository employeeRepository;
    public CommentController(CommentRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }
    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/getComments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getComments(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
        employeeRepository.save( new Employee(UUID.randomUUID().toString(), "b", "2L")).subscribe(System.out::println);
        return "Success";

    }
}
