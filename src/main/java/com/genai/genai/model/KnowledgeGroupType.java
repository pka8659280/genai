package com.genai.genai.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "knowledge_group_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgeGroupType {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;
    @Lob
    @Column(name = "system_instruction", columnDefinition = "TEXT")
    private String systemInstruction;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_edit_date_time")
    private LocalDateTime lastEditDateTime;

    @Column(name = "created_by_user_id", nullable = false, updatable = false)
    private Long createdByUserId;

    @Column(name = "last_edited_by_user_id")
    private Long lastEditedByUserId;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (deleted == null) {
            deleted = false;
        }
        createdDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastEditDateTime = LocalDateTime.now();
    }
}
