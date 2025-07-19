package com.personalnote.service;

import com.personalnote.dto.NoteTemplateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteTemplateService {

    /**
     * 创建模板
     */
    NoteTemplateDTO createTemplate(NoteTemplateDTO templateDTO);

    /**
     * 更新模板
     */
    NoteTemplateDTO updateTemplate(Long id, NoteTemplateDTO templateDTO);

    /**
     * 删除模板
     */
    void deleteTemplate(Long id);

    /**
     * 根据ID获取模板
     */
    NoteTemplateDTO getTemplateById(Long id);

    /**
     * 获取所有模板
     */
    List<NoteTemplateDTO> getAllTemplates();

    /**
     * 分页获取模板
     */
    Page<NoteTemplateDTO> getTemplates(Pageable pageable);

    /**
     * 根据分类获取模板
     */
    List<NoteTemplateDTO> getTemplatesByCategory(String category);

    /**
     * 获取系统模板
     */
    List<NoteTemplateDTO> getSystemTemplates();

    /**
     * 获取用户自定义模板
     */
    List<NoteTemplateDTO> getUserTemplates();

    /**
     * 搜索模板
     */
    Page<NoteTemplateDTO> searchTemplates(String keyword, Pageable pageable);

    /**
     * 获取所有分类
     */
    List<String> getAllCategories();

    /**
     * 使用模板创建笔记
     */
    void useTemplate(Long templateId);

    /**
     * 初始化系统默认模板
     */
    void initializeSystemTemplates();
}