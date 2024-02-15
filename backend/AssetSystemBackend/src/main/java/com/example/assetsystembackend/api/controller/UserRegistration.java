package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.service.DynamicService;
import com.example.assetsystembackend.api.user.BaseUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/user")
public class UserRegistration {

    private final BaseUser user;

    public UserRegistration(BaseUser user){
        this.user = user;
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> addUser(@RequestBody Map<String, Object> data){

        boolean valid;
        //check data validity

        String username = (String) data.get("user");
        if (!checkValidity(username)) {
            return ResponseEntity.badRequest().body("Invalid username.");
        }

        String password = (String) data.get("password");
        if (!checkValidity(password)){
            return ResponseEntity.badRequest().body("Invalid password.");
        }

        user.newUser(username, password);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered.");

    }

    public boolean checkValidity(String data){
        //add validity checks as necessary
        return data.length() >= 6;
    }
}
