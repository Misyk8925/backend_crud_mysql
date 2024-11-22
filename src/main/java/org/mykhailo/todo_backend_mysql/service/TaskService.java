package org.mykhailo.todo_backend_mysql.service;

import org.mykhailo.todo_backend_mysql.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks ();

    Task saveTask (Task task);

    Task getTaskById (long id);

    Task getTaskByTitle (String title);

    Task updateTask (Task task);

    void deleteTask (long id);
}
