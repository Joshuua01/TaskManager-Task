package com.project.taskmanager.task.context.task;

import com.project.taskmanager.task.context.task.dto.TaskRequest;
import com.project.taskmanager.task.context.task.dto.TaskResponse;
import com.project.taskmanager.task.domain.task.Task;
import com.project.taskmanager.task.domain.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    @Value("${variables.auth-uri}")
    private String AuthUri;
    @Value("${variables.internal-secret}")
    private String SecretInternal;

    private WebClient getUserWebClient(UUID id) {
        return WebClient.builder()
                .baseUrl(AuthUri + "user/internal/" + id.toString())
                .defaultHeader("AuthInt", SecretInternal)
                .build();
    }
    private boolean userExists(UUID id) {
        WebClient client = getUserWebClient(id);

        return Boolean.TRUE.equals(client.get().exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    if (status.is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                })
                .block());
    }

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
                .creatorName(getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .updatedById(task.getUpdatedById().toString())
                .updatedByName(getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
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
                .creatorName(getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .assigneeId(task.getAssigneeId() == null ? null : task.getAssigneeId().toString())
                .assigneeName(task.getAssigneeId() == null ? null : getUserWebClient(task.getAssigneeId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .updatedById(task.getUpdatedById().toString())
                .updatedByName(getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
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
                    .creatorName(getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                    .assigneeId(task.getAssigneeId() == null ? null : task.getAssigneeId().toString())
                    .assigneeName(task.getAssigneeId() == null ? null : getUserWebClient(task.getAssigneeId()).get().retrieve().bodyToMono(String.class).block())
                    .createdAt(task.getCreatedAt().toString())
                    .updatedAt(task.getUpdatedAt().toString())
                    .updatedById(task.getUpdatedById().toString())
                    .updatedByName(getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
                    .build();
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getAssigneeId() != null && !userExists(request.getAssigneeId())) {
            throw new RuntimeException("Assignee not found");
        }

        task.setTitle(request.getTitle() != null ? request.getTitle() : task.getTitle());
        task.setDescription(request.getDescription() != null ? request.getDescription() : task.getDescription());
        task.setAssigneeId(request.getAssigneeId() != null ? request.getAssigneeId() : task.getAssigneeId());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedById(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()));
        taskRepository.save(task);

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .creatorId(task.getCreatorId().toString())
                .creatorName(getUserWebClient(task.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .assigneeId(task.getAssigneeId() == null ? null : task.getAssigneeId().toString())
                .assigneeName(task.getAssigneeId() == null ? null : getUserWebClient(task.getAssigneeId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(task.getCreatedAt().toString())
                .updatedAt(task.getUpdatedAt().toString())
                .updatedById(task.getUpdatedById().toString())
                .updatedByName(getUserWebClient(task.getUpdatedById()).get().retrieve().bodyToMono(String.class).block())
                .build();
    }

    public void deleteTask(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
