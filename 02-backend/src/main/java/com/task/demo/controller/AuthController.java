package com.task.demo.controller;

import com.task.demo.dao.RoleRepository;
import com.task.demo.dao.UserRepository;
import com.task.demo.model.EnumRole;
import com.task.demo.model.Role;
import com.task.demo.model.User;
import com.task.demo.payload.request.LoginRequest;
import com.task.demo.payload.request.RegisterRequest;
import com.task.demo.payload.response.JwtResponse;
import com.task.demo.payload.response.MessageResponse;
import com.task.demo.security.JwtUtils;
import com.task.demo.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
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
    JwtUtils jwtUtils;

    // Login with existing credentials
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getBirthday()
        ));
    }

    // Register the user in Database
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        // Check if the username is used
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if the email is used
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(EnumRole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(EnumRole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(EnumRole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setBirthday(registerRequest.getBirthday());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    // Update existing user account
    @PutMapping("/profile-edit/{id}")
    public ResponseEntity<?> updateUserInfo(@Valid @PathVariable("id") Long id, @RequestBody RegisterRequest updateUserRequest) {

        // Create User Object based on the id number
        Optional<User> userFromDB = userRepository.findById(id);

        // Check if the user is present
        if (userFromDB.isPresent()) {
            User updatedUser = userFromDB.get();

            // Check if the user new "username" is different from previous
            if (updatedUser.getUsername().equals(updateUserRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Used an old username, try another one, please!"));
            }

            // Check if the user new "email" is different from previous
            if (updatedUser.getEmail().equals(updateUserRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Used an old email, try another one, please!"));
            }

            // Check if the username is used
            if (userRepository.existsByUsername(updateUserRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            }

            // Check if the email is used
            if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            // Get current user from DB
            userFromDB.get().setUsername(updateUserRequest.getUsername());
            userFromDB.get().setEmail(updateUserRequest.getEmail());
            userFromDB.get().setPassword(encoder.encode(updateUserRequest.getPassword()));
            userFromDB.get().setFirstName(updateUserRequest.getFirstName());
            userFromDB.get().setLastName(updateUserRequest.getLastName());
            userFromDB.get().setBirthday(updateUserRequest.getBirthday());

            userRepository.save(updatedUser);

            return ResponseEntity.ok(new MessageResponse("User information is updated successfully!"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is not found"));
        }
    }
}
