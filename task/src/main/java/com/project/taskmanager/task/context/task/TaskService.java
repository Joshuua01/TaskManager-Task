package com.project.taskmanager.task.context.task;

import com.project.taskmanager.task.context.task.dto.TaskRequest;
import com.project.taskmanager.task.context.task.dto.TaskResponse;
import com.project.taskmanager.task.domain.Task;
import com.project.taskmanager.task.domain.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskResponse createTask(TaskRequest request) {
        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creatorId(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()))
                .assigneeId(request.getAssigneeId())
                .build();
        taskRepository.save(task);

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .creatorId(task.getCreatorId().toString())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .build();
    }

    public TaskResponse getTaskById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .creatorId(task.getCreatorId().toString())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .build();
    }

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Task task : tasks) {
            TaskResponse taskResponse = TaskResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus().name())
                    .creatorId(task.getCreatorId().toString())
                    .createdAt(task.getCreatedAt().toString())
                    .updatedAt(task.getUpdatedAt().toString())
                    .build();
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(request.getTitle() != null ? request.getTitle() : task.getTitle());
        task.setDescription(request.getDescription() != null ? request.getDescription() : task.getDescription());
        task.setAssigneeId(request.getAssigneeId() != null ? request.getAssigneeId() : task.getAssigneeId());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .creatorId(task.getCreatorId().toString())
                .assigneeId(task.getAssigneeId().toString() != null ? task.getAssigneeId().toString() : null)
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .build();
    }
}
