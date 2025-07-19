package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIGrammarCheckResponse {
    
    private String correctedText; // 修正后的文本
    
    private String originalText; // 原始文本
    
    private List<GrammarError> errors; // 语法错误列表
    
    private Integer errorCount; // 错误数量
    
    private Double grammarScore; // 语法分数 0-100
    
    private String language; // 语言
    
    private List<String> suggestions; // 改进建议
    
    private LocalDateTime checkedTime; // 检查时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, failed
    
    private String errorMessage; // 错误信息
    
    @Data
    public static class GrammarError {
        private String errorType; // spelling, grammar, punctuation, style
        private String description; // 错误描述
        private String original; // 原始文本片段
        private String suggestion; // 建议修正
        private Integer startIndex; // 错误开始位置
        private Integer endIndex; // 错误结束位置
        private String severity; // low, medium, high
        private Double confidence; // 置信度
    }
}