package com.personalnote.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareLinkDTO {
    private Long id;

    private Long noteId;

    private String shareToken;

    private String sharePassword;

    private LocalDateTime expireTime;

    private Boolean allowEdit;

    private Boolean allowDownload;

    private Integer viewCount;

    private Boolean isActive;

    private String createdBy;

    private LocalDateTime lastAccessedTime;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    // 获取完整的分享链接
    public String getFullShareUrl(String baseUrl) {
        return baseUrl + "/share/" + shareToken;
    }

    // 检查是否已过期
    public Boolean isExpired() {
        if (expireTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expireTime);
    }

    // 获取剩余有效时间（小时）
    public Long getRemainingHours() {
        if (expireTime == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expireTime)) {
            return 0L;
        }
        return java.time.Duration.between(now, expireTime).toHours();
    }
}