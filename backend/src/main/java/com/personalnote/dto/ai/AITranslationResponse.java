package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AITranslationResponse {
    
    private String translatedText; // 翻译后文本
    
    private String originalText; // 原始文本
    
    private String fromLanguage; // 源语言
    
    private String toLanguage; // 目标语言
    
    private String detectedLanguage; // 自动检测的语言
    
    private Double confidence; // 置信度
    
    private List<String> alternativeTranslations; // 其他翻译选项
    
    private String translationMethod; // neural, statistical, hybrid
    
    private Boolean isAutoDetected; // 是否自动检测语言
    
    private String textType; // formal, informal, technical, literary
    
    private List<TranslationSegment> segments; // 分段翻译
    
    private LocalDateTime translatedTime; // 翻译时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private Integer tokensUsed; // 消耗的Token数量
    
    private String status; // success, partial, failed
    
    private String errorMessage; // 错误信息
    
    @Data
    public static class TranslationSegment {
        private String originalSegment;
        private String translatedSegment;
        private Double confidence;
    }
}