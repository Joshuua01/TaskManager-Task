package com.project.taskmanager.task.context.task;

import com.project.taskmanager.task.context.task.dto.TaskUpdateRequest;
import com.project.taskmanager.task.domain.Task;
import com.project.taskmanager.task.infrastructure.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetTaskByIdWithNonExistingTaskExpectExceptionThrown() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(taskId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Task not found");

        verify(taskRepository, times(1)).findById(taskId);
    }


    @Test
    void testUpdateTaskWithNonExistingTaskExpectExceptionThrown() {
        Long taskId = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Task");
        request.setDescription("This is an updated task");
        request.setAssigneeId(UUID.randomUUID());

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(taskId, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Task not found");

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testDeleteTaskWithExistingTaskExpectTaskDeletion() {
        Long taskId = 1L;
        Task task = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTaskWithNonExistingTaskExpectExceptionThrown() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(taskId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Task not found");

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).delete(any());
    }
}