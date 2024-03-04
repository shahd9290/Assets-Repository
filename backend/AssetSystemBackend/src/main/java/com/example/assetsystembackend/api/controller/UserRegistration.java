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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRegistration {

    BaseUser user;
    JdbcTemplate template;
    DataSource dataSource;

    @Autowired
    public UserRegistration(JdbcTemplate template, BaseUser user, DataSource dataSource) {
        this.template = template;
        this.user = user;
        this.dataSource = dataSource;
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> addUser(@RequestBody Map<String, String> data) throws SQLException {

        //check data validity

        String username = data.get("username");
        if (existing(username)) {
            return ResponseEntity.badRequest().body("Username already exists.");
        } else if (invalidUsername(username)) {
            return ResponseEntity.badRequest().body("Invalid username.");
        }

        String password = data.get("password");
        if (!checkValidity(password)) {
            return ResponseEntity.badRequest().body("Invalid password.");
        }

        String level = data.get("user-level");

        switch (level) {
            case "admin":
                user.newAdmin(username, password);
                break;
            case "general":
                user.newGeneral(username, password);
                break;
            case "viewer":
                user.newViewer(username, password);
                break;
            default:
                ResponseEntity.badRequest().body("Invalid user request.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("User registered.");

    }

    public boolean checkValidity (String data){
        //add validity checks as necessary
        return data.length() > 7;
    }

    public boolean existing (String data) throws SQLException {
        try (Connection con = dataSource.getConnection();) {
            PreparedStatement ps = con.prepareStatement("select * from pg_roles where rolname = ?");
            ps.setString(1, data);

            ResultSet rs = ps.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean invalidUsername (String username){
        return username.length() > 7;
    }

}
