package org.mykhailo.todo_backend_mysql.repository;

import org.mykhailo.todo_backend_mysql.model.Task;
import org.mykhailo.todo_backend_mysql.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    void deleteTaskByTitle(String title);

    Task findTaskByTitle(String title);

    List<Task> findAllByUser(User user);

    Task findTaskById(long id);
}
