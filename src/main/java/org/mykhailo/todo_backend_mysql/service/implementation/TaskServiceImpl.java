package org.mykhailo.todo_backend_mysql.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.mykhailo.todo_backend_mysql.model.Task;
import org.mykhailo.todo_backend_mysql.repository.TaskRepository;
import org.mykhailo.todo_backend_mysql.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task saveTask(Task task) {
        System.out.println("Start: " + task);
        Task savedTask = taskRepository.save(task);
        System.out.println(savedTask);
        System.out.println();
        return savedTask;
    }

    @Override
    public Task getTaskById(long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task getTaskByTitle(String title) {
        return taskRepository.findTaskByTitle(title);
    }

    @Override
    public Task updateTask(Task task) {

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(String title) {
        taskRepository.deleteTaskByTitle(title);
    }

}
