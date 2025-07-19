package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIWritingSuggestionResponse {
    
    private List<WritingSuggestion> suggestions; // 写作建议列表
    
    private String overallScore; // 整体评分 A-F
    
    private Double readabilityScore; // 可读性分数
    
    private String targetAudience; // 目标受众
    
    private String tone; // 语调分析
    
    private String style; // 风格分析
    
    private List<String> strengths; // 优点
    
    private List<String> improvements; // 改进点
    
    private String language; // 语言
    
    private LocalDateTime analyzedTime; // 分析时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, failed
    
    private String errorMessage; // 错误信息
    
    @Data
    public static class WritingSuggestion {
        private String type; // clarity, conciseness, engagement, structure, vocabulary
        private String title; // 建议标题
        private String description; // 详细描述
        private String example; // 示例
        private String originalText; // 原文片段
        private String improvedText; // 改进后文本
        private String priority; // high, medium, low
        private Integer startIndex; // 开始位置
        private Integer endIndex; // 结束位置
    }
}