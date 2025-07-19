package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class AIImageDescriptionResponse {
    
    private String description; // 图片描述
    
    private String detailedDescription; // 详细描述
    
    private List<String> objects; // 识别出的物体
    
    private List<String> scenes; // 场景识别
    
    private List<String> colors; // 主要颜色
    
    private List<String> activities; // 活动/动作
    
    private Map<String, Double> emotions; // 情感识别（如果是人脸）
    
    private String style; // 艺术风格（如果适用）
    
    private String composition; // 构图分析
    
    private Double confidence; // 置信度
    
    private String imageFormat; // 图片格式
    
    private Integer imageWidth; // 图片宽度
    
    private Integer imageHeight; // 图片高度
    
    private Boolean isPhotograph; // 是否为照片
    
    private Boolean hasText; // 是否包含文字
    
    private Boolean hasFaces; // 是否包含人脸
    
    private Integer faceCount; // 人脸数量
    
    private String quality; // 图片质量评估
    
    private LocalDateTime analyzedTime; // 分析时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, failed
    
    private String errorMessage; // 错误信息
}