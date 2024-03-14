package com.example.assetsystembackend.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/test-security")
public class TestController {
    @GetMapping("/all")
    public String allAccess(){
        return "Public Content.";
    }

    @GetMapping("/viewer")
    @PreAuthorize("hasRole('VIEWER') or hasRole('USER') or hasRole('ADMIN')")
    public String viewerAccess() {
        return "Viewer Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess() {
        return "User Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }


}
