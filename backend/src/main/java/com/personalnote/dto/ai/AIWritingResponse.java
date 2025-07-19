package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIWritingResponse {
    
    private String content; // 生成的内容
    
    private String originalTopic; // 原始主题
    
    private String style; // 使用的风格
    
    private String type; // 文档类型
    
    private Integer wordCount; // 字数统计
    
    private Integer charCount; // 字符数统计
    
    private Double confidence; // 置信度 0-1
    
    private List<String> suggestions; // 改进建议
    
    private List<String> keywords; // 提取的关键词
    
    private String language; // 语言
    
    private LocalDateTime generatedTime; // 生成时间
    
    private String requestId; // 请求ID，用于追踪
    
    private String model; // 使用的AI模型
    
    private Integer tokensUsed; // 消耗的Token数量
    
    private String status; // success, partial, failed
    
    private String errorMessage; // 错误信息（如果有）
}