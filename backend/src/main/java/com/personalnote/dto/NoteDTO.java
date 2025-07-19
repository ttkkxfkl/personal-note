package com.personalnote.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteDTO {
    private Long id;

    @NotBlank(message = "笔记标题不能为空")
    @Size(max = 200, message = "笔记标题长度不能超过200个字符")
    private String title;

    private String content;

    private String markdownContent;

    private Boolean isFavorite;

    private Boolean isPublic;

    private Integer viewCount;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private NotebookDTO notebook;

    private List<TagDTO> tags;
}