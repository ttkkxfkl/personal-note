package com.personalnote.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentDTO {
    private Long id;

    private String filename;

    private String originalFilename;

    private String filePath;

    private Long fileSize;

    private String mimeType;

    private String fileType;

    private Long noteId;

    private Integer downloadCount;

    private Boolean isImage;

    private String thumbnailPath;

    private String altText;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    // 格式化文件大小的便利方法
    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));
        return String.format("%.1f %s", 
            fileSize / Math.pow(1024, digitGroups), 
            units[digitGroups]);
    }
}