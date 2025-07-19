package com.personalnote.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "attachment")
public class Attachment extends BaseEntity {

    @NotBlank(message = "文件名不能为空")
    @Size(max = 255, message = "文件名长度不能超过255个字符")
    @Column(name = "filename", nullable = false, length = 255)
    private String filename;

    @NotBlank(message = "原始文件名不能为空")
    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "file_type", length = 20)
    private String fileType; // IMAGE, DOCUMENT, VIDEO, AUDIO, OTHER

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    @Column(name = "download_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer downloadCount = 0;

    @Column(name = "is_image", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isImage = false;

    @Column(name = "thumbnail_path", length = 500)
    private String thumbnailPath;

    @Column(name = "alt_text", length = 200)
    private String altText;
}