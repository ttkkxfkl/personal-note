package com.personalnote.service;

import com.personalnote.dto.TagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    /**
     * 创建标签
     */
    TagDTO createTag(TagDTO tagDTO);

    /**
     * 更新标签
     */
    TagDTO updateTag(Long id, TagDTO tagDTO);

    /**
     * 删除标签（软删除）
     */
    void deleteTag(Long id);

    /**
     * 根据ID获取标签
     */
    TagDTO getTagById(Long id);

    /**
     * 获取所有标签
     */
    List<TagDTO> getAllTags();

    /**
     * 分页获取标签
     */
    Page<TagDTO> getTags(Pageable pageable);

    /**
     * 搜索标签
     */
    Page<TagDTO> searchTags(String keyword, Pageable pageable);

    /**
     * 根据名称列表获取或创建标签
     */
    List<TagDTO> getOrCreateTagsByNames(List<String> tagNames);
}