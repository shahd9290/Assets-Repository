package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.service.DynamicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/user")
public class UserRegistration {

    private DynamicService service;

    public UserRegistration(DynamicService service){
        this.service = service;
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> addUser(@RequestBody Map<String, Object> data){
        //check data validity

        String username = (String) data.get("user");
        String password = (String) data.get("password");
        service.newUser(username, password);

        return ResponseEntity.status(HttpStatus.CREATED).body("Data added successfully");

    }
}
