package com.personalnote.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NotebookDTO {
    private Long id;

    @NotBlank(message = "笔记本名称不能为空")
    @Size(max = 100, message = "笔记本名称长度不能超过100个字符")
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    private String color;

    private Integer sortOrder;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Long noteCount;
}