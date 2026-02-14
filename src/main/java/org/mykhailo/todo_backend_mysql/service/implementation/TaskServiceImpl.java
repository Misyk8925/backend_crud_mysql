package org.mykhailo.todo_backend_mysql.service.implementation;

import jakarta.transaction.Transactional;
import org.mykhailo.todo_backend_mysql.model.Task;
import org.mykhailo.todo_backend_mysql.model.User;
import org.mykhailo.todo_backend_mysql.repository.TaskRepository;
import org.mykhailo.todo_backend_mysql.service.TaskService;
import org.mykhailo.todo_backend_mysql.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    private User getAuthenticatedUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        return currentUser;
    }

    private boolean isOwnedByCurrentUser(Task task, User currentUser) {
        return task != null
                && task.getUser() != null
                && Objects.equals(task.getUser().getId(), currentUser.getId());
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
        if (isOwnedByCurrentUser(task, currentUser)) {
            return taskRepository.save(task);
        }
        return null;
    }

    @Override
    public void deleteTask(long id) {

        User currentUser = getAuthenticatedUser();

        Task task = taskRepository.findTaskById(id);
        if (task == null) {
            return;
        }
        if (task.getUser() == null) {
            task.setUser(currentUser);
        }
        if (isOwnedByCurrentUser(task, currentUser)) {
            taskRepository.deleteById(id);
        }
    }

    @Override
    public Task getTaskById(long id) {
        User currentUser = getAuthenticatedUser();
        Task task = taskRepository.findById(id).orElse(null);
        if (isOwnedByCurrentUser(task, currentUser)) {
            return task;
        }
        return null;
    }

    @Override
    public Task getTaskByTitle(String title) {
        User currentUser = getAuthenticatedUser();
        Task task = taskRepository.findTaskByTitle(title);
        if (isOwnedByCurrentUser(task, currentUser)) {
            return task;
        }
        return null;
    }


}
