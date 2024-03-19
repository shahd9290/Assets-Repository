package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.models.ERole;
import com.example.assetsystembackend.api.models.Role;
import com.example.assetsystembackend.api.models.User;
import com.example.assetsystembackend.api.payload.request.LoginRequest;
import com.example.assetsystembackend.api.payload.request.SignupRequest;
import com.example.assetsystembackend.api.payload.response.JwtResponse;
import com.example.assetsystembackend.api.payload.response.MessageResponse;
import com.example.assetsystembackend.api.repository.RoleRepository;
import com.example.assetsystembackend.api.repository.UserRepository;
import com.example.assetsystembackend.api.security.jwt.JwtUtils;
import com.example.assetsystembackend.api.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
            public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "hello";
        if (authentication.isAuthenticated()) {
             jwt = jwtUtils.generateJwtToken(authentication);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();
        String role;
        //if (roles.contains(ERole.ROLE_ADMIN.toString())){
        if (Objects.equals(roles.get(0), ERole.ROLE_ADMIN.toString())) {
            role = "admin";
        } else if (Objects.equals(roles.get(0), ERole.ROLE_USER.toString())) {
            role = "user";
        }else{
            role = "viewer";
        }
        JwtResponse test = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), role);

        return ResponseEntity.ok(test);
    }

    @PostMapping("/signup")
            public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if (userRepo.existsByUsername((signupRequest.getUsername()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepo.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email already in use!"));
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role viewerRole = roleRepo.findByName(ERole.ROLE_VIEWER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(viewerRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(adminRole);
                    }
                    case "user" -> {
                        Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(userRole);
                    }
                    default -> {
                        Role viewerRole = roleRepo.findByName(ERole.ROLE_VIEWER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(viewerRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully."));
    }
}
