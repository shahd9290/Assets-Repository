package com.example.assetsystembackend.api.controller;

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

    private final JdbcTemplate template;

    @Autowired
    public UserRegistration(JdbcTemplate template){
        this.template = template;
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

        newUser(username, password);

        return ResponseEntity.status(HttpStatus.OK).body("User registered.");

    }

    public boolean checkValidity(String data){
        //add validity checks as necessary
        return data.length() >= 6;
    }

    public void newUser(String username, String password){
        String query = "CREATE USER " + username +
                " WITH\nLOGIN\nSUPERUSER\nINHERIT\nCREATEDB\nCREATEROLE\nREPLICATION\n ENCRYPTED PASSWORD '"
                + password + "';\nGRANT pg_signal_backend TO " + username + " WITH ADMIN OPTION;";
        template.execute(query);
    }
}
