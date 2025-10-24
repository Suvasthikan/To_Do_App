package com.todo.repository;

import com.todo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByCompletedFalseOrderByCreatedAtDesc();
    
    List<Task> findAllByOrderByCreatedAtDesc();
    
    Page<Task> findByCompletedFalseOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Task> findAllByOrderByCreatedAtDesc(Pageable pageable);
}