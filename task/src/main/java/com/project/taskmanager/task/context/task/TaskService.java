package com.project.taskmanager.task.context.task;

import com.project.taskmanager.task.context.task.dto.TaskRequest;
import com.project.taskmanager.task.context.task.dto.TaskResponse;
import com.project.taskmanager.task.context.task.dto.TaskUpdateRequest;
import com.project.taskmanager.task.domain.task.Task;
import com.project.taskmanager.task.domain.task.TaskRepository;
import com.project.taskmanager.task.infrastructure.UserUtility;
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
    private final UserUtility userUtility;

    public TaskResponse createTask(TaskRequest request) {
        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creatorId(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()))
                .assigneeId(request.getAssigneeId())
                .updatedById(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()))
                .build();
        taskRepository.save(task);

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .creatorId(task.getCreatorId().toString())
                .creatorName(userUtility.getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .updatedById(task.getUpdatedById().toString())
                .updatedByName(userUtility.getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
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
                .creatorName(userUtility.getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .assigneeId(task.getAssigneeId() == null ? null : task.getAssigneeId().toString())
                .assigneeName(task.getAssigneeId() == null ? null : userUtility.getUserWebClient(task.getAssigneeId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .updatedById(task.getUpdatedById().toString())
                .updatedByName(userUtility.getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
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
                    .creatorName(userUtility.getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                    .assigneeId(task.getAssigneeId() == null ? null : task.getAssigneeId().toString())
                    .assigneeName(task.getAssigneeId() == null ? null : userUtility.getUserWebClient(task.getAssigneeId()).get().retrieve().bodyToMono(String.class).block())
                    .createdAt(task.getCreatedAt().toString())
                    .updatedAt(task.getUpdatedAt().toString())
                    .updatedById(task.getUpdatedById().toString())
                    .updatedByName(userUtility.getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
                    .build();
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getAssigneeId() != null && !userUtility.userExists(request.getAssigneeId())) {
            throw new RuntimeException("Assignee not found");
        }

        task.setTitle(request.getTitle() != null ? request.getTitle() : task.getTitle());
        task.setDescription(request.getDescription() != null ? request.getDescription() : task.getDescription());
        task.setAssigneeId(request.getAssigneeId() != null ? request.getAssigneeId() : task.getAssigneeId());
        task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedById(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()));
        taskRepository.save(task);

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .creatorId(task.getCreatorId().toString())
                .creatorName(userUtility.getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .assigneeId(task.getAssigneeId() == null ? null : task.getAssigneeId().toString())
                .assigneeName(task.getAssigneeId() == null ? null : userUtility.getUserWebClient(task.getAssigneeId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .updatedById(task.getUpdatedById().toString())
                .updatedByName(userUtility.getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
                .build();
    }

    public void deleteTask(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
