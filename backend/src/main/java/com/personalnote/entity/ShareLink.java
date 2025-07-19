package com.personalnote.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "share_link")
public class ShareLink extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @Column(name = "share_token", nullable = false, unique = true, length = 64)
    private String shareToken;

    @Column(name = "share_password", length = 20)
    private String sharePassword;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    @Column(name = "allow_edit", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean allowEdit = false;

    @Column(name = "allow_download", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean allowDownload = true;

    @Column(name = "view_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive = true;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "last_accessed_time")
    private LocalDateTime lastAccessedTime;
}