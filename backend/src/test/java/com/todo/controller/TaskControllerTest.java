package com.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.dto.CreateTaskRequest;
import com.todo.dto.TaskDTO;
import com.todo.service.TaskNotFoundException;
import com.todo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for TaskController
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private TaskService taskService;
    
    private TaskDTO taskDTO;
    
    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO(1L, "Test Task", "Test Description", false, LocalDateTime.now());
    }
    
    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest("New Task", "New Description");
        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(taskDTO);
        
        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.completed").value(false));
        
        verify(taskService, times(1)).createTask(any(CreateTaskRequest.class));
    }
    
    @Test
    void createTask_ShouldReturnBadRequest_WhenTitleIsBlank() throws Exception {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest("", "Description");
        
        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        
        verify(taskService, never()).createTask(any(CreateTaskRequest.class));
    }
    
    @Test
    void createTask_ShouldReturnBadRequest_WhenDescriptionIsBlank() throws Exception {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest("Title", "");
        
        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        
        verify(taskService, never()).createTask(any(CreateTaskRequest.class));
    }
    
    @Test
    void getRecentTasks_ShouldReturnListOfTasks() throws Exception {
        // Arrange
        List<TaskDTO> tasks = Arrays.asList(taskDTO);
        when(taskService.getFirstPageIncompleteTasks()).thenReturn(tasks);
        
        // Act & Assert
        mockMvc.perform(get("/api/tasks/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
        
        verify(taskService, times(1)).getFirstPageIncompleteTasks();
    }
    
    @Test
    void getAllTasks_ShouldReturnListOfTasks() throws Exception {
        // Arrange
        List<TaskDTO> tasks = Arrays.asList(taskDTO);
        when(taskService.getAllTasks()).thenReturn(tasks);
        
        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        
        verify(taskService, times(1)).getAllTasks();
    }
    
    @Test
    void completeTask_ShouldReturnUpdatedTask() throws Exception {
        // Arrange
        taskDTO.setCompleted(true);
        when(taskService.markTaskAsCompleted(1L)).thenReturn(taskDTO);
        
        // Act & Assert
        mockMvc.perform(put("/api/tasks/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
        
        verify(taskService, times(1)).markTaskAsCompleted(1L);
    }
    
    @Test
    void completeTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        when(taskService.markTaskAsCompleted(999L))
                .thenThrow(new TaskNotFoundException("Task not found"));
        
        // Act & Assert
        mockMvc.perform(put("/api/tasks/999/complete"))
                .andExpect(status().isNotFound());
        
        verify(taskService, times(1)).markTaskAsCompleted(999L);
    }
    
    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
        
        verify(taskService, times(1)).deleteTask(1L);
    }
    
    @Test
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Arrange
        doThrow(new TaskNotFoundException("Task not found"))
                .when(taskService).deleteTask(999L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
        
        verify(taskService, times(1)).deleteTask(999L);
    }
}



