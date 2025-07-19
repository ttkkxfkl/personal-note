package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AISimilarityResponse {
    
    private List<SimilarNote> similarNotes; // 相似笔记列表
    
    private String queryContent; // 查询内容
    
    private String similarityMethod; // cosine, jaccard, semantic
    
    private Double averageSimilarity; // 平均相似度
    
    private Integer totalFound; // 找到的总数
    
    private LocalDateTime searchedTime; // 搜索时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, failed
    
    private String errorMessage; // 错误信息
    
    @Data
    public static class SimilarNote {
        private Long noteId;
        private String title;
        private String snippet; // 内容片段
        private Double similarity; // 相似度分数
        private String reason; // 相似原因
        private List<String> matchingKeywords; // 匹配的关键词
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
    }
}