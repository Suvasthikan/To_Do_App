package com.todo.controller;

import com.todo.dto.CreateTaskRequest;
import com.todo.dto.TaskDTO;
import com.todo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {
    
    private final TaskService taskService;
    
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskDTO createdTask = taskService.createTask(request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<TaskDTO>> getFirstPageTasks() {
        List<TaskDTO> tasks = taskService.getFirstPageIncompleteTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/page/{page}")
    public ResponseEntity<Page<TaskDTO>> getTasksPage(@PathVariable int page) {
        Page<TaskDTO> taskPage = taskService.getIncompleteTasks(page);
        return ResponseEntity.ok(taskPage);
    }
    
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getIncompleteTaskCount() {
        long count = taskService.getIncompleteTaskCount();
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskDTO> completeTask(@PathVariable Long id) {
        TaskDTO updatedTask = taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok(updatedTask);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}