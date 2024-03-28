package assetsystem.backend.api.controller;

import assetsystem.backend.api.model.ERole;
import assetsystem.backend.api.model.Role;
import assetsystem.backend.api.model.User;
import assetsystem.backend.api.payload.request.LoginRequest;
import assetsystem.backend.api.payload.request.SignupRequest;
import assetsystem.backend.api.payload.response.JwtResponse;
import assetsystem.backend.api.repository.RoleRepository;
import assetsystem.backend.api.repository.UserRepository;
import assetsystem.backend.api.security.jwt.JwtUtils;
import assetsystem.backend.api.service.UserDetailsImpl;
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

/**
 * Controller used to manage users, including creation and authentication.
 */
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

    /**
     * Authenticates a user by validating the provided login credentials and generates a JWT token upon successful authentication.
     *
     * @param loginRequest The login request containing the username and password to authenticate.
     * @return A summary of the user details, including the token used throughout the system.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Use credentials to authenticate
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Initialize JWT token;
        String jwt = "";
        // Generate token.
        if (authentication.isAuthenticated()) {
            jwt = jwtUtils.generateJwtToken(authentication);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract user details.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();
        String role;
        if (Objects.equals(roles.get(0), ERole.ROLE_ADMIN.toString())) {
            role = "admin";
        } else if (Objects.equals(roles.get(0), ERole.ROLE_USER.toString())) {
            role = "user";
        } else {
            role = "viewer";
        }
        // Output
        JwtResponse test = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), role);

        return ResponseEntity.ok(test);
    }

    /**
     * Registers a new user with the provided signup information.
     *
     * @param signupRequest The signup request containing user details such as username, email, and password.
     * @return A response dependent on whether the user has been created, already exists or details are missing.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        // Check if email and username are already taken.
        if (userRepo.existsByUsername((signupRequest.getUsername()))) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        if (userRepo.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email already in use!");
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        // Check if roles are valid. If none provided then return exception.
        // Else assign the corresponding role to the user.
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

        return ResponseEntity.ok("User Registered Successfully");
    }
}
