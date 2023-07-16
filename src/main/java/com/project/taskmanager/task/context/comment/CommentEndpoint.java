package com.project.taskmanager.task.context.comment;

import com.project.taskmanager.task.context.comment.dto.CommentRequest;
import com.project.taskmanager.task.context.comment.dto.CommentResponse;
import com.project.taskmanager.task.infrastructure.UserUtility;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentEndpoint {
    private final UserUtility userUtility;
    private final CommentService commentService;

    @Operation(summary = "Create a new comment for desired task", description = "Returns the created comment")
    @PostMapping("/create/{taskId}")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long taskId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(taskId, request));
    }

    @Operation(summary = "Get comment by id", description = "Returns the comment with the given id")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        if (!userUtility.isAdmin())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @Operation(summary = "Get all comments", description = "Returns all comments")
    @GetMapping("/all")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        if (!userUtility.isAdmin())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @Operation(summary = "Get all comments by task id", description = "Returns all comments by task id")
    @GetMapping("/all/{taskId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getAllCommentsByTaskId(taskId));
    }

    @Operation(summary = "Update comment by id", description = "Returns the updated comment")
    @PatchMapping("/update/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        if (!userUtility.isAdmin() && !userUtility.isCommentCreator(id))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @Operation(summary = "Delete comment by id", description = "Returns a message if the comment was deleted successfully")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        if (!userUtility.isAdmin() && !userUtility.isCommentCreator(id))
            return ResponseEntity.badRequest().build();
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

}
