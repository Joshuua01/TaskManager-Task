package com.project.taskmanager.task.context.attachment;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentEndpoint {

    private final AttachmentService attachmentService;

    @PostMapping("/upload/{taskId}")
    public ResponseEntity<String> uploadAttachment(@RequestBody MultipartFile file, @PathVariable Long taskId) {
        return ResponseEntity.ok(attachmentService.uploadAttachment(file, taskId));
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long attachmentId) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachmentService.downloadAttachment(attachmentId).getType()))
                .body(attachmentService.downloadAttachment(attachmentId).getData());
    }
}
