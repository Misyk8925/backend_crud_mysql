package org.mykhailo.todo_backend_mysql.controller;

import lombok.AllArgsConstructor;
import org.mykhailo.todo_backend_mysql.model.DTO.LoginRequest;
import org.mykhailo.todo_backend_mysql.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class AuthController {


    private UserService userService;

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
