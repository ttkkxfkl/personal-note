package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AITranscriptionResponse {
    
    private String text; // 转录文本
    
    private String language; // 检测到的语言
    
    private Double confidence; // 置信度
    
    private Integer duration; // 音频时长（秒）
    
    private List<TimestampedText> segments; // 分段文本（带时间戳）
    
    private List<String> speakers; // 说话人识别
    
    private String audioFormat; // 音频格式
    
    private Integer audioSampleRate; // 采样率
    
    private Boolean hasMultipleSpeakers; // 是否有多个说话人
    
    private String punctuation; // 标点符号处理后的文本
    
    private LocalDateTime transcribedTime; // 转录时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, partial, failed
    
    private String errorMessage; // 错误信息
    
    @Data
    public static class TimestampedText {
        private String text;
        private Double startTime;
        private Double endTime;
        private Double confidence;
        private String speaker;
    }
}