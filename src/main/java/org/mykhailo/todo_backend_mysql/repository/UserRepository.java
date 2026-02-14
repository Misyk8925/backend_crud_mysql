package org.mykhailo.todo_backend_mysql.repository;

import org.mykhailo.todo_backend_mysql.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

}
