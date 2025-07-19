package com.personalnote.dto.ai;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AIChatMessage {
    
    private String role; // user, assistant, system
    
    private String content; // 消息内容
    
    private LocalDateTime timestamp; // 时间戳
    
    private String messageId; // 消息ID
    
    private String messageType; // text, image, file
    
    private String metadata; // 额外元数据
    
    public AIChatMessage() {}
    
    public AIChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
    
    public static AIChatMessage user(String content) {
        return new AIChatMessage("user", content);
    }
    
    public static AIChatMessage assistant(String content) {
        return new AIChatMessage("assistant", content);
    }
    
    public static AIChatMessage system(String content) {
        return new AIChatMessage("system", content);
    }
}