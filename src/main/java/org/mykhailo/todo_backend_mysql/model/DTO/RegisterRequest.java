package org.mykhailo.todo_backend_mysql.model.DTO;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;

    private String password;
}
