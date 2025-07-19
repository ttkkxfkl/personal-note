package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class AIClassificationResponse {
    
    private String primaryCategory; // 主要分类
    
    private Double primaryConfidence; // 主要分类置信度
    
    private List<CategoryScore> allCategories; // 所有分类及其分数
    
    private List<String> suggestedTags; // 推荐标签
    
    private String contentType; // article, note, email, report, etc.
    
    private String topic; // 主题
    
    private String domain; // 领域：technology, business, science, etc.
    
    private String sentiment; // 情感倾向
    
    private String language; // 语言
    
    private String complexity; // 复杂度：simple, moderate, complex
    
    private Map<String, String> metadata; // 额外元数据
    
    private LocalDateTime classifiedTime; // 分类时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, failed
    
    private String errorMessage; // 错误信息
    
    @Data
    public static class CategoryScore {
        private String category;
        private Double score;
        private String description;
    }
}