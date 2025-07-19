package com.personalnote.repository;

import com.personalnote.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * 查找未删除的笔记列表
     */
    @Query("SELECT n FROM Note n WHERE n.isDeleted = false ORDER BY n.updatedTime DESC")
    List<Note> findAllActive();

    /**
     * 分页查找未删除的笔记
     */
    @Query("SELECT n FROM Note n WHERE n.isDeleted = false")
    Page<Note> findAllActive(Pageable pageable);

    /**
     * 根据ID查找未删除的笔记
     */
    @Query("SELECT n FROM Note n WHERE n.id = :id AND n.isDeleted = false")
    Optional<Note> findActiveById(@Param("id") Long id);

    /**
     * 根据笔记本ID查找笔记
     */
    @Query("SELECT n FROM Note n WHERE n.notebook.id = :notebookId AND n.isDeleted = false ORDER BY n.updatedTime DESC")
    Page<Note> findByNotebookId(@Param("notebookId") Long notebookId, Pageable pageable);

    /**
     * 查找收藏的笔记
     */
    @Query("SELECT n FROM Note n WHERE n.isFavorite = true AND n.isDeleted = false ORDER BY n.updatedTime DESC")
    Page<Note> findFavoriteNotes(Pageable pageable);

    /**
     * 根据标签ID查找笔记
     */
    @Query("SELECT n FROM Note n JOIN n.tags t WHERE t.id = :tagId AND n.isDeleted = false ORDER BY n.updatedTime DESC")
    Page<Note> findByTagId(@Param("tagId") Long tagId, Pageable pageable);

    /**
     * 全文搜索笔记
     */
    @Query("SELECT n FROM Note n WHERE n.isDeleted = false AND " +
           "(n.title LIKE %:keyword% OR n.content LIKE %:keyword% OR n.markdownContent LIKE %:keyword%)")
    Page<Note> searchNotes(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 查找回收站中的笔记
     */
    @Query("SELECT n FROM Note n WHERE n.isDeleted = true ORDER BY n.updatedTime DESC")
    Page<Note> findDeletedNotes(Pageable pageable);

    /**
     * 增加浏览次数
     */
    @Modifying
    @Query("UPDATE Note n SET n.viewCount = n.viewCount + 1 WHERE n.id = :id")
    void incrementViewCount(@Param("id") Long id);

    /**
     * 统计笔记本中的笔记数量
     */
    @Query("SELECT COUNT(n) FROM Note n WHERE n.notebook.id = :notebookId AND n.isDeleted = false")
    Long countByNotebookId(@Param("notebookId") Long notebookId);

    /**
     * 统计标签的笔记数量
     */
    @Query("SELECT COUNT(n) FROM Note n JOIN n.tags t WHERE t.id = :tagId AND n.isDeleted = false")
    Long countByTagId(@Param("tagId") Long tagId);
}