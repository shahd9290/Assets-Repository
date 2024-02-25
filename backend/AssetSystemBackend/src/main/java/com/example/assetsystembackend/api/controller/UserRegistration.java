package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.user.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRegistration {

    BaseUser user;

    @Autowired
    public UserRegistration(JdbcTemplate template, BaseUser user){
        this.user = user;
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> addUser(@RequestBody Map<String, String> data){

        //check data validity

        String username = data.get("user");
        if (!checkValidity(username)) {
            return ResponseEntity.badRequest().body("Invalid username.");
        }

        String password = data.get("password");
        if (!checkValidity(password)){
            return ResponseEntity.badRequest().body("Invalid password.");
        }

        user.newUser(username, password);

        return ResponseEntity.status(HttpStatus.OK).body("User registered.");

    }

    public boolean checkValidity(String data){
        //add validity checks as necessary
        return data.length() >= 6;
    }

}
