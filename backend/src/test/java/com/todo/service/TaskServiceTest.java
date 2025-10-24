package com.todo.service;

import com.todo.dto.CreateTaskRequest;
import com.todo.dto.TaskDTO;
import com.todo.model.Task;
import com.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskService
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @InjectMocks
    private TaskService taskService;
    
    private Task task1;
    private Task task2;
    
    @BeforeEach
    void setUp() {
        task1 = new Task(1L, "Task 1", "Description 1", false, LocalDateTime.now());
        task2 = new Task(2L, "Task 2", "Description 2", false, LocalDateTime.now().minusHours(1));
    }
    
    @Test
    void createTask_ShouldReturnCreatedTask() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest("New Task", "New Description");
        Task savedTask = new Task(1L, "New Task", "New Description", false, LocalDateTime.now());
        
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        
        // Act
        TaskDTO result = taskService.createTask(request);
        
        // Assert
        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertFalse(result.getCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    
    @Test
    void getFirstPageIncompleteTasks_ShouldReturnMaxFiveTasks() {
        // Arrange
        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findByCompletedFalseOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(taskPage);
        
        // Act
        List<TaskDTO> result = taskService.getFirstPageIncompleteTasks();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findByCompletedFalseOrderByCreatedAtDesc(any(Pageable.class));
    }
    
    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAllByOrderByCreatedAtDesc()).thenReturn(tasks);
        
        // Act
        List<TaskDTO> result = taskService.getAllTasks();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }
    
    @Test
    void markTaskAsCompleted_ShouldUpdateTaskStatus() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        task1.setCompleted(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task1);
        
        // Act
        TaskDTO result = taskService.markTaskAsCompleted(1L);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    
    @Test
    void markTaskAsCompleted_ShouldThrowException_WhenTaskNotFound() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.markTaskAsCompleted(999L);
        });
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }
    
    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);
        
        // Act
        taskService.deleteTask(1L);
        
        // Assert
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        // Arrange
        when(taskRepository.existsById(999L)).thenReturn(false);
        
        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(999L);
        });
        verify(taskRepository, times(1)).existsById(999L);
        verify(taskRepository, never()).deleteById(anyLong());
    }
}



