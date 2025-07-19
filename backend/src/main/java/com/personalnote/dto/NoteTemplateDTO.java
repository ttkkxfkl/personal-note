package com.personalnote.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NoteTemplateDTO {
    private Long id;

    @NotBlank(message = "模板名称不能为空")
    @Size(max = 100, message = "模板名称长度不能超过100个字符")
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    private String category;

    private String contentTemplate;

    private String markdownTemplate;

    private Boolean isSystem;

    private Integer usageCount;

    private String icon;

    private String color;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}