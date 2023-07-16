package com.project.taskmanager.task.context.attachment;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentEndpoint {

    private final AttachmentService attachmentService;

    @Operation(summary = "Upload an attachment", description = "Returns the message if attachment was updated successfully")
    @PostMapping("/upload/{taskId}")
    public ResponseEntity<String> uploadAttachment(@RequestBody MultipartFile file, @PathVariable Long taskId) {
        return ResponseEntity.ok(attachmentService.uploadAttachment(file, taskId));
    }

    @Operation(summary = "Download an attachment", description = "Returns the attachment")
    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long attachmentId) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachmentService.downloadAttachment(attachmentId).getType()))
                .body(attachmentService.downloadAttachment(attachmentId).getData());
    }

    @Operation(summary = "Delete an attachment", description = "Returns the message if attachment was deleted successfully")
    @DeleteMapping("/delete/{attachmentId}")
    public ResponseEntity<String> deleteAttachment(@PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.ok("Attachment deleted successfully");
    }
}
