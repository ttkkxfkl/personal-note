package com.personalnote.service;

import com.personalnote.dto.NoteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {

    /**
     * 创建笔记
     */
    NoteDTO createNote(NoteDTO noteDTO);

    /**
     * 更新笔记
     */
    NoteDTO updateNote(Long id, NoteDTO noteDTO);

    /**
     * 删除笔记（软删除）
     */
    void deleteNote(Long id);

    /**
     * 根据ID获取笔记
     */
    NoteDTO getNoteById(Long id);

    /**
     * 分页获取所有笔记
     */
    Page<NoteDTO> getAllNotes(Pageable pageable);

    /**
     * 根据笔记本ID分页获取笔记
     */
    Page<NoteDTO> getNotesByNotebookId(Long notebookId, Pageable pageable);

    /**
     * 根据标签ID分页获取笔记
     */
    Page<NoteDTO> getNotesByTagId(Long tagId, Pageable pageable);

    /**
     * 获取收藏的笔记
     */
    Page<NoteDTO> getFavoriteNotes(Pageable pageable);

    /**
     * 搜索笔记
     */
    Page<NoteDTO> searchNotes(String keyword, Pageable pageable);

    /**
     * 获取回收站笔记
     */
    Page<NoteDTO> getDeletedNotes(Pageable pageable);

    /**
     * 切换收藏状态
     */
    NoteDTO toggleFavorite(Long id);

    /**
     * 恢复笔记
     */
    NoteDTO restoreNote(Long id);

    /**
     * 永久删除笔记
     */
    void permanentDeleteNote(Long id);

    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);
}