package com.project.taskmanager.task.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserUtility {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    @Value("${variables.auth-uri}")
    private String AuthUri;
    @Value("${variables.internal-secret}")
    private String SecretInternal;


    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    public boolean isCommentCreator(Long commentId) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return comment.getCreatorId().toString().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public boolean isTaskCreator(Long taskId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return task.getCreatorId().toString().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }


    public WebClient getUserWebClient(UUID id) {
        return WebClient.builder()
                .baseUrl(AuthUri + "internal/" + id.toString())
                .defaultHeader("AuthInt", SecretInternal)
                .build();
    }

    public boolean userExists(UUID id) {
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
}
