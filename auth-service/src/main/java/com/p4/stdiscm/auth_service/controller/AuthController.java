package com.p4.stdiscm.auth_service.controller;

import com.p4.stdiscm.auth_service.model.User;
import com.p4.stdiscm.auth_service.model.Role;
import com.p4.stdiscm.auth_service.repository.UserRepository;
import com.p4.stdiscm.auth_service.repository.RoleRepository;
import com.p4.stdiscm.auth_service.service.JwtTokenService;
import com.p4.stdiscm.auth_service.payload.request.SignupRequest;
import com.p4.stdiscm.auth_service.payload.request.LoginRequest;
import com.p4.stdiscm.auth_service.payload.response.JwtResponse;
import com.p4.stdiscm.auth_service.payload.response.MessageResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenService.generateToken(authentication);
        
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        // Existing validation code...
        
        // Log the incoming roles in the request
        System.out.println("SIGNUP REQUEST - Username: " + signUpRequest.getUsername());
        System.out.println("SIGNUP REQUEST - Email: " + signUpRequest.getEmail());
        System.out.println("SIGNUP REQUEST - Roles requested: " + signUpRequest.getRoles());
        
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                        encoder.encode(signUpRequest.getPassword()),        
                        signUpRequest.getEmail()
                        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            System.out.println("SIGNUP - No roles specified, using default ROLE_USER");
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                System.out.println("SIGNUP - Processing role: " + role);
                switch (role) {
                case "admin":
                    System.out.println("SIGNUP - Adding ROLE_ADMIN");
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                    break;
                case "mod":
                    System.out.println("SIGNUP - Adding ROLE_MODERATOR");
                    Role modRole = roleRepository.findByName("ROLE_MODERATOR")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);
                    break;
                default:
                    System.out.println("SIGNUP - Adding default ROLE_USER for role: " + role);
                    Role userRole = roleRepository.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        
        // Log the roles before saving
        System.out.println("SIGNUP - Roles being saved for user:");
        roles.forEach(role -> System.out.println("  Role: " + role.getName() + ", ID: " + role.getId()));
        
        userRepository.save(user);
        
        // Verify the roles after saving
        User savedUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (savedUser != null) {
            System.out.println("SIGNUP - Roles after saving for user " + savedUser.getUsername() + ":");
            savedUser.getRoles().forEach(role -> 
                System.out.println("  Role: " + role.getName() + ", ID: " + role.getId()));
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Get the authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && 
            !(authentication instanceof AnonymousAuthenticationToken)) {
            
            String username = authentication.getName();
            System.out.println("Logging out user: " + username);
            
            // Invalidate token in Redis
            jwtTokenService.invalidateToken(username);
            
            // Clear the security context
            SecurityContextHolder.clearContext();
            
            return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
        }
        
        return ResponseEntity.ok(new MessageResponse("No active session"));
    }
}
