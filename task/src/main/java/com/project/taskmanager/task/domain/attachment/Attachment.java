package com.project.taskmanager.task.domain.attachment;

import com.project.taskmanager.task.domain.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "attachments")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
