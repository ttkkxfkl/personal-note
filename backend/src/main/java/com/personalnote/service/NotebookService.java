package com.personalnote.service;

import com.personalnote.dto.NotebookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotebookService {

    /**
     * 创建笔记本
     */
    NotebookDTO createNotebook(NotebookDTO notebookDTO);

    /**
     * 更新笔记本
     */
    NotebookDTO updateNotebook(Long id, NotebookDTO notebookDTO);

    /**
     * 删除笔记本（软删除）
     */
    void deleteNotebook(Long id);

    /**
     * 根据ID获取笔记本
     */
    NotebookDTO getNotebookById(Long id);

    /**
     * 获取所有笔记本
     */
    List<NotebookDTO> getAllNotebooks();

    /**
     * 分页获取笔记本
     */
    Page<NotebookDTO> getNotebooks(Pageable pageable);

    /**
     * 搜索笔记本
     */
    Page<NotebookDTO> searchNotebooks(String keyword, Pageable pageable);
}