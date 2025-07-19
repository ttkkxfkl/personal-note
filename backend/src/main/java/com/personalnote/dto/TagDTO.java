package com.personalnote.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class TagDTO {
    private Long id;

    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称长度不能超过50个字符")
    private String name;

    private String color;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Long noteCount;
}