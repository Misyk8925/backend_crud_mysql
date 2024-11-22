package org.mykhailo.todo_backend_mysql.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.mykhailo.todo_backend_mysql.model.Task;
import org.mykhailo.todo_backend_mysql.model.User;
import org.mykhailo.todo_backend_mysql.repository.TaskRepository;
import org.mykhailo.todo_backend_mysql.service.TaskService;
import org.mykhailo.todo_backend_mysql.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    private User getAuthenticatedUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        return currentUser;
    }

    @Override
    public List<Task> getAllTasks() {
        User currentUser = getAuthenticatedUser();
        return taskRepository.findAllByUser(currentUser);
    }

    @Override
    public Task saveTask(Task task) {
        User currentUser = getAuthenticatedUser();
        task.setUser(currentUser);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        User currentUser = getAuthenticatedUser();
        if (task.getUser() == null) {
            task.setUser(currentUser);
        }
        if (task.getUser().equals(currentUser)) {
            return taskRepository.save(task);
        }
        return null;
    }

    @Override
    public void deleteTask(long id) {

        User currentUser = getAuthenticatedUser();

        Task task = taskRepository.findTaskById(id);
        if (task.getUser() == null) {
            task.setUser(currentUser);
        }
        if (task != null && task.getUser().equals(currentUser)) {
            taskRepository.deleteById(id);
        }
    }

    @Override
    public Task getTaskById(long id) {
        User currentUser = getAuthenticatedUser();
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null && task.getUser().equals(currentUser)) {
            return task;
        }
        return null;
    }

    @Override
    public Task getTaskByTitle(String title) {
        User currentUser = getAuthenticatedUser();
        Task task = taskRepository.findTaskByTitle(title);
        if (task != null && task.getUser().equals(currentUser)) {
            return task;
        }
        return null;
    }


}