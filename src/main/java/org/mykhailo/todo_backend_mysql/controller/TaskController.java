package org.mykhailo.todo_backend_mysql.controller;

import lombok.AllArgsConstructor;
import org.mykhailo.todo_backend_mysql.model.Task;
import org.mykhailo.todo_backend_mysql.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping("/save")
    public Task saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @PutMapping("/update")
    public Task updateTask(@RequestBody Task task) {
        return taskService.updateTask(task);
    }

    @DeleteMapping("/delete/{title}")
    public void deleteTask(@PathVariable String title) {
        taskService.deleteTask(title);
    }
}
