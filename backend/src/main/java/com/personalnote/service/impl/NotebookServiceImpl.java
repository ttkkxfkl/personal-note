package com.personalnote.service.impl;

import com.personalnote.dto.NotebookDTO;
import com.personalnote.entity.Notebook;
import com.personalnote.repository.NotebookRepository;
import com.personalnote.repository.NoteRepository;
import com.personalnote.service.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotebookServiceImpl implements NotebookService {

    private final NotebookRepository notebookRepository;
    private final NoteRepository noteRepository;

    @Override
    public NotebookDTO createNotebook(NotebookDTO notebookDTO) {
        // 检查名称是否重复
        if (notebookRepository.findActiveByName(notebookDTO.getName()).isPresent()) {
            throw new RuntimeException("笔记本名称已存在");
        }

        Notebook notebook = convertToEntity(notebookDTO);
        notebook = notebookRepository.save(notebook);
        return convertToDTO(notebook);
    }

    @Override
    public NotebookDTO updateNotebook(Long id, NotebookDTO notebookDTO) {
        Notebook existingNotebook = notebookRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("笔记本不存在"));

        // 检查名称是否重复（排除自己）
        notebookRepository.findActiveByName(notebookDTO.getName())
                .ifPresent(notebook -> {
                    if (!notebook.getId().equals(id)) {
                        throw new RuntimeException("笔记本名称已存在");
                    }
                });

        existingNotebook.setName(notebookDTO.getName());
        existingNotebook.setDescription(notebookDTO.getDescription());
        existingNotebook.setColor(notebookDTO.getColor());
        existingNotebook.setSortOrder(notebookDTO.getSortOrder());

        existingNotebook = notebookRepository.save(existingNotebook);
        return convertToDTO(existingNotebook);
    }

    @Override
    public void deleteNotebook(Long id) {
        Notebook notebook = notebookRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("笔记本不存在"));

        notebook.setIsDeleted(true);
        notebookRepository.save(notebook);
    }

    @Override
    @Transactional(readOnly = true)
    public NotebookDTO getNotebookById(Long id) {
        Notebook notebook = notebookRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("笔记本不存在"));
        return convertToDTO(notebook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotebookDTO> getAllNotebooks() {
        return notebookRepository.findAllActive().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotebookDTO> getNotebooks(Pageable pageable) {
        return notebookRepository.findAllActive(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotebookDTO> searchNotebooks(String keyword, Pageable pageable) {
        return notebookRepository.searchNotebooks(keyword, pageable)
                .map(this::convertToDTO);
    }

    private NotebookDTO convertToDTO(Notebook notebook) {
        NotebookDTO dto = new NotebookDTO();
        dto.setId(notebook.getId());
        dto.setName(notebook.getName());
        dto.setDescription(notebook.getDescription());
        dto.setColor(notebook.getColor());
        dto.setSortOrder(notebook.getSortOrder());
        dto.setCreatedTime(notebook.getCreatedTime());
        dto.setUpdatedTime(notebook.getUpdatedTime());
        
        // 获取笔记数量
        Long noteCount = noteRepository.countByNotebookId(notebook.getId());
        dto.setNoteCount(noteCount);
        
        return dto;
    }

    private Notebook convertToEntity(NotebookDTO dto) {
        Notebook notebook = new Notebook();
        notebook.setName(dto.getName());
        notebook.setDescription(dto.getDescription());
        notebook.setColor(dto.getColor());
        notebook.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        return notebook;
    }
}