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
@Table(name = "tag")
public class Tag extends BaseEntity {

    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "color", length = 20)
    private String color;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Note> notes;
}