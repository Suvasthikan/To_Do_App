package com.todo.repository;

import com.todo.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for TaskRepository
 */
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TaskRepositoryTest {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }
    
    @Test
    void findByCompletedFalseOrderByCreatedAtDesc_ShouldReturnIncompleteTasks() {
        // Arrange
        Task task1 = new Task(null, "Task 1", "Description 1", false, null);
        Task task2 = new Task(null, "Task 2", "Description 2", true, null);
        Task task3 = new Task(null, "Task 3", "Description 3", false, null);
        
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
        
        // Act
        List<Task> incompleteTasks = taskRepository.findByCompletedFalseOrderByCreatedAtDesc();
        
        // Assert
        assertEquals(2, incompleteTasks.size());
        assertTrue(incompleteTasks.stream().noneMatch(Task::getCompleted));
    }
    
    @Test
    void findAllByOrderByCreatedAtDesc_ShouldReturnAllTasksInDescendingOrder() {
        // Arrange
        Task task1 = new Task(null, "Task 1", "Description 1", false, null);
        Task task2 = new Task(null, "Task 2", "Description 2", true, null);
        
        taskRepository.save(task1);
        taskRepository.save(task2);
        
        // Act
        List<Task> allTasks = taskRepository.findAllByOrderByCreatedAtDesc();
        
        // Assert
        assertEquals(2, allTasks.size());
        // Most recent task should be first
        assertTrue(allTasks.get(0).getCreatedAt().isAfter(allTasks.get(1).getCreatedAt()) ||
                   allTasks.get(0).getCreatedAt().isEqual(allTasks.get(1).getCreatedAt()));
    }
    
    @Test
    void save_ShouldPersistTask() {
        // Arrange
        Task task = new Task(null, "New Task", "New Description", false, null);
        
        // Act
        Task savedTask = taskRepository.save(task);
        
        // Assert
        assertNotNull(savedTask.getId());
        assertNotNull(savedTask.getCreatedAt());
        assertEquals("New Task", savedTask.getTitle());
        assertEquals("New Description", savedTask.getDescription());
        assertFalse(savedTask.getCompleted());
    }
}



