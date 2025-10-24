package com.todo.service;

import com.todo.dto.CreateTaskRequest;
import com.todo.dto.TaskDTO;
import com.todo.model.Task;
import com.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    private static final int PAGE_SIZE = 5;
    
    @Transactional
    public TaskDTO createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);
        
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
    
    @Transactional(readOnly = true)
    public List<TaskDTO> getFirstPageIncompleteTasks() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Page<Task> taskPage = taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable);
        return taskPage.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<TaskDTO> getIncompleteTasks(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Task> taskPage = taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable);
        return taskPage.map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TaskDTO markTaskAsCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }
    
    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
    
    @Transactional(readOnly = true)
    public long getIncompleteTaskCount() {
        return taskRepository.findByCompletedFalseOrderByCreatedAtDesc().size();
    }
    
    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCompleted(),
            task.getCreatedAt()
        );
    }
}