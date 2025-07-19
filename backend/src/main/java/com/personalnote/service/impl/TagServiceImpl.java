package com.personalnote.service.impl;

import com.personalnote.dto.TagDTO;
import com.personalnote.repository.TagRepository;
import com.personalnote.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        // TODO: 实现创建标签逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        // TODO: 实现更新标签逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public void deleteTag(Long id) {
        // TODO: 实现删除标签逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public TagDTO getTagById(Long id) {
        // TODO: 实现获取标签逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        // 返回空列表以避免前端报错
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TagDTO> getTags(Pageable pageable) {
        // TODO: 实现分页获取标签逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TagDTO> searchTags(String keyword, Pageable pageable) {
        // TODO: 实现搜索标签逻辑
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public List<TagDTO> getOrCreateTagsByNames(List<String> tagNames) {
        // TODO: 实现根据名称获取或创建标签逻辑
        throw new UnsupportedOperationException("暂未实现");
    }
}