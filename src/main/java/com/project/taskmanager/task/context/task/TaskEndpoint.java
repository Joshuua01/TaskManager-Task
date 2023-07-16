package com.project.taskmanager.task.context.task;

import com.project.taskmanager.task.context.task.dto.TaskRequest;
import com.project.taskmanager.task.context.task.dto.TaskResponse;
import com.project.taskmanager.task.context.task.dto.TaskUpdateRequest;
import com.project.taskmanager.task.infrastructure.UserUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskEndpoint {
    private final UserUtility userUtility;
    private final TaskService taskService;

    @Operation(summary = "Create a new task", description = "Returns the created task")
    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @Operation(summary = "Get task by id", description = "Returns the task with the given id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Operation(summary = "Get all tasks", description = "Returns all tasks")
    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Update task by id", description = "Returns the updated task")
    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request) {
        if (!userUtility.isAdmin() && !userUtility.isTaskCreator(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @Operation(summary = "Delete task by id", description = "Returns a message if the task was deleted successfully")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        if (!userUtility.isAdmin() && !userUtility.isTaskCreator(id)) {
            return ResponseEntity.badRequest().build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}

