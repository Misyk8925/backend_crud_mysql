package org.mykhailo.todo_backend_mysql.model.DTO;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;
}
