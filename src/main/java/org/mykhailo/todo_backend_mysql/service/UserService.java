package org.mykhailo.todo_backend_mysql.service;

import org.mykhailo.todo_backend_mysql.model.DTO.LoginRequest;
import org.mykhailo.todo_backend_mysql.model.DTO.RegisterRequest;
import org.mykhailo.todo_backend_mysql.model.User;
import org.mykhailo.todo_backend_mysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository repo;


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (registerRequest.getEmail() == null || registerRequest.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (repo.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        return repo.save(user);
    }

    public String verify(LoginRequest loginRequest) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(loginRequest.getUsername());
            }
            return "fail";
        } catch (BadCredentialsException exception) {
            return "Invalid credentials";
        }
    }

    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = repo.findByUsername(username);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }
        return currentUser;
    }

}
      
