package org.mykhailo.todo_backend_mysql.controller;

import org.mykhailo.todo_backend_mysql.model.DTO.LoginRequest;
import org.mykhailo.todo_backend_mysql.model.DTO.RegisterRequest;
import org.mykhailo.todo_backend_mysql.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("http://localhost:5173")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered");
        } catch (IllegalArgumentException exception) {
            if ("Username already exists".equals(exception.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
            }
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        String loginResponse = userService.verify(loginRequest);
        if (loginResponse.equals("fail") || loginResponse.equals("Invalid credentials")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        } else {
            return ResponseEntity.ok(loginResponse);
        }
    }
}
