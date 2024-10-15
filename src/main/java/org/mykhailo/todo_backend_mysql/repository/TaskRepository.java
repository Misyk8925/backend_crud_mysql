package org.mykhailo.todo_backend_mysql.repository;

import org.mykhailo.todo_backend_mysql.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    void deleteTaskByTitle(String title);

    Task findTaskByTitle(String title);
}
