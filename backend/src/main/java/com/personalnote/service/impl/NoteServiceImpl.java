package com.personalnote.service.impl;

import com.personalnote.dto.NoteDTO;
import com.personalnote.entity.Note;
import com.personalnote.repository.NoteRepository;
import com.personalnote.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public NoteDTO createNote(NoteDTO noteDTO) {
        // TODO: 实现创建笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public NoteDTO updateNote(Long id, NoteDTO noteDTO) {
        // TODO: 实现更新笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public void deleteNote(Long id) {
        // TODO: 实现删除笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public NoteDTO getNoteById(Long id) {
        // TODO: 实现获取笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> getAllNotes(Pageable pageable) {
        // TODO: 实现获取所有笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> getNotesByNotebookId(Long notebookId, Pageable pageable) {
        // TODO: 实现根据笔记本获取笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> getNotesByTagId(Long tagId, Pageable pageable) {
        // TODO: 实现根据标签获取笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> getFavoriteNotes(Pageable pageable) {
        // TODO: 实现获取收藏笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> searchNotes(String keyword, Pageable pageable) {
        // TODO: 实现搜索笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> getDeletedNotes(Pageable pageable) {
        // TODO: 实现获取回收站笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public NoteDTO toggleFavorite(Long id) {
        // TODO: 实现切换收藏状态逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public NoteDTO restoreNote(Long id) {
        // TODO: 实现恢复笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public void permanentDeleteNote(Long id) {
        // TODO: 实现永久删除笔记逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public void incrementViewCount(Long id) {
        // TODO: 实现增加浏览次数逻辑
        throw new UnsupportedOperationException("暂未实现");
    }
}