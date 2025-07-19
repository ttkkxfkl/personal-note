package com.personalnote.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notebook")
public class Notebook extends BaseEntity {

    @NotBlank(message = "笔记本名称不能为空")
    @Size(max = 100, message = "笔记本名称长度不能超过100个字符")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "sort_order", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Note> notes;
}