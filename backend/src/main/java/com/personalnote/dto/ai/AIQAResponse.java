package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIQAResponse {
    
    private String answer; // 回答内容
    
    private String question; // 原始问题
    
    private Double confidence; // 置信度
    
    private List<String> sourceReferences; // 引用来源
    
    private List<String> relatedQuestions; // 相关问题推荐
    
    private String answerType; // factual, opinion, instruction, explanation
    
    private Boolean hasMultipleAnswers; // 是否有多个可能答案
    
    private List<String> alternativeAnswers; // 其他可能答案
    
    private String contextUsed; // 使用的上下文
    
    private LocalDateTime answeredTime; // 回答时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private Integer tokensUsed; // 消耗的Token数量
    
    private String status; // success, uncertain, failed
    
    private String errorMessage; // 错误信息
}