package com.personalnote.dto.ai;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AIWritingRequest {
    
    @NotBlank(message = "主题不能为空")
    @Size(max = 200, message = "主题长度不能超过200个字符")
    private String topic;
    
    @Size(max = 50, message = "写作风格长度不能超过50个字符")
    private String style; // formal, casual, academic, creative, business
    
    @Size(max = 50, message = "文档类型长度不能超过50个字符")
    private String type; // article, email, report, summary, outline
    
    private Integer length; // 期望长度：1-短，2-中，3-长
    
    @Size(max = 500, message = "上下文长度不能超过500个字符")
    private String context; // 额外上下文信息
    
    @Size(max = 100, message = "语言长度不能超过100个字符")
    private String language; // zh-CN, en-US
    
    @Size(max = 300, message = "要求长度不能超过300个字符")
    private String requirements; // 特殊要求
}