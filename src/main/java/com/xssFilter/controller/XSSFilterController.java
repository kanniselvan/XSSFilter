package com.xssFilter.controller;

import com.xssFilter.model.Employee;
import org.springframework.web.bind.annotation.*;

@RestController
public class XSSFilterController {

    @GetMapping("/hello/{message}")
    public String show(@PathVariable String message){
        return message;

    }

    @GetMapping("/welcome")
    public String welcome(@RequestParam String message,@RequestParam String message1){
        return message;

    }

    @PostMapping("/test")
    public String test(@RequestBody Employee employee){
        return "Post call sucessfuly";

    }
}
