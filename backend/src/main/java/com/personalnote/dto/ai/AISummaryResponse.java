package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AISummaryResponse {
    
    private String summary; // 生成的摘要
    
    private String originalContent; // 原始内容的摘要
    
    private Integer originalLength; // 原始内容长度
    
    private Integer summaryLength; // 摘要长度
    
    private Double compressionRatio; // 压缩比例
    
    private List<String> keyPoints; // 关键要点
    
    private List<String> mainTopics; // 主要话题
    
    private String summaryType; // extractive, abstractive
    
    private Double relevanceScore; // 相关性评分
    
    private LocalDateTime generatedTime; // 生成时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private Integer tokensUsed; // 消耗的Token数量
    
    private String status; // success, partial, failed
    
    private String errorMessage; // 错误信息
}