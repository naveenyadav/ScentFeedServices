package com.scent.feedservices.controller;

import com.scent.feedservices.EventHandler;
import com.scent.feedservices.data.Employee;
import com.scent.feedservices.data.EventData;
import com.scent.feedservices.repositories.PostRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
    private EmployeeRepository employeeRepository;
    public CommentController(EmployeeRepository employeeRepository){
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

    public static void main(String[] args) {
        EventHandler<Integer> ss = new EventHandler<Integer>();
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(ss);
    }


    /**
     * This POST controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/postComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void postComment(@RequestBody Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
