package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIOCRResponse {
    
    private String extractedText; // 提取的文本
    
    private String language; // 检测到的语言
    
    private Double confidence; // 置信度
    
    private List<TextBlock> textBlocks; // 文本块（带位置信息）
    
    private String imageFormat; // 图片格式
    
    private Integer imageWidth; // 图片宽度
    
    private Integer imageHeight; // 图片高度
    
    private String orientation; // 文本方向
    
    private Boolean hasTable; // 是否包含表格
    
    private List<TableData> tables; // 表格数据
    
    private LocalDateTime processedTime; // 处理时间
    
    private String requestId; // 请求ID
    
    private String model; // 使用的AI模型
    
    private String status; // success, partial, failed
    
    private String errorMessage; // 错误信息
    
    @Data
    public static class TextBlock {
        private String text;
        private Double confidence;
        private BoundingBox boundingBox;
        private String type; // word, line, paragraph
    }
    
    @Data
    public static class BoundingBox {
        private Integer x;
        private Integer y;
        private Integer width;
        private Integer height;
    }
    
    @Data
    public static class TableData {
        private List<List<String>> rows;
        private Integer columnCount;
        private Integer rowCount;
        private BoundingBox boundingBox;
    }
}