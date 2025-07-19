package com.personalnote.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "note_template")
public class NoteTemplate extends BaseEntity {

    @NotBlank(message = "模板名称不能为空")
    @Size(max = 100, message = "模板名称长度不能超过100个字符")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category", length = 50)
    private String category;

    @Lob
    @Column(name = "content_template", columnDefinition = "LONGTEXT")
    private String contentTemplate;

    @Lob
    @Column(name = "markdown_template", columnDefinition = "LONGTEXT")
    private String markdownTemplate;

    @Column(name = "is_system", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isSystem = false;

    @Column(name = "usage_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer usageCount = 0;

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "color", length = 20)
    private String color;
}