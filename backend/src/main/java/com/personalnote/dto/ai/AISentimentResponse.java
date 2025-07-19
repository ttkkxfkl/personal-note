package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class AISentimentResponse {
    
    private String overallSentiment; // POSITIVE, NEGATIVE, NEUTRAL
    
    private Double positiveScore; // 积极情感分数 0-1
    
    private Double negativeScore; // 消极情感分数 0-1
    
    private Double neutralScore; // 中性情感分数 0-1
    
    private Double confidence; // 置信度
    
    private Map<String, Double> emotions; // 详细情感分析：joy, anger, fear, sadness, surprise, disgust
    
    private String dominantEmotion; // 主导情感
    
    private Double emotionIntensity; // 情感强度 0-1
    
    private String textAnalyzed; // 分析的文本片段
    
    private Integer sentenceCount; // 句子数量
    
    private Map<String, String> sentenceSentiments; // 每句话的情感分析
    
    private LocalDateTime analyzedTime; // 分析时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, failed
    
    private String errorMessage; // 错误信息
}