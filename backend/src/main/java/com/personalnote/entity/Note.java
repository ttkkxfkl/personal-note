package com.personalnote.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "note")
public class Note extends BaseEntity {

    @NotBlank(message = "笔记标题不能为空")
    @Size(max = 200, message = "笔记标题长度不能超过200个字符")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Lob
    @Column(name = "markdown_content", columnDefinition = "LONGTEXT")
    private String markdownContent;

    @Column(name = "is_favorite", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFavorite = false;

    @Column(name = "is_public", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isPublic = false;

    @Column(name = "view_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notebook_id")
    private Notebook notebook;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "note_tag",
        joinColumns = @JoinColumn(name = "note_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}