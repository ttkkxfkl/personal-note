package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIChatResponse {
    
    private String response; // AI回复内容
    
    private String conversationId; // 对话ID
    
    private String messageId; // 消息ID
    
    private String userMessage; // 用户消息
    
    private List<String> suggestedQuestions; // 建议问题
    
    private String responseType; // informative, creative, analytical, conversational
    
    private Double confidence; // 置信度
    
    private List<String> sources; // 引用来源
    
    private String mood; // AI回复的情绪
    
    private Boolean needsFollowUp; // 是否需要后续问题
    
    private String contextUsed; // 使用的上下文
    
    private LocalDateTime responseTime; // 回复时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private Integer tokensUsed; // 消耗的Token数量
    
    private String status; // success, failed
    
    private String errorMessage; // 错误信息
}