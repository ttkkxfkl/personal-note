package com.personalnote.repository;

import com.personalnote.entity.NoteTemplate;
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
public interface NoteTemplateRepository extends JpaRepository<NoteTemplate, Long> {

    /**
     * 查找未删除的模板列表
     */
    @Query("SELECT nt FROM NoteTemplate nt WHERE nt.isDeleted = false ORDER BY nt.usageCount DESC, nt.createdTime DESC")
    List<NoteTemplate> findAllActive();

    /**
     * 分页查找未删除的模板
     */
    @Query("SELECT nt FROM NoteTemplate nt WHERE nt.isDeleted = false")
    Page<NoteTemplate> findAllActive(Pageable pageable);

    /**
     * 根据ID查找未删除的模板
     */
    @Query("SELECT nt FROM NoteTemplate nt WHERE nt.id = :id AND nt.isDeleted = false")
    Optional<NoteTemplate> findActiveById(@Param("id") Long id);

    /**
     * 根据分类查找模板
     */
    @Query("SELECT nt FROM NoteTemplate nt WHERE nt.category = :category AND nt.isDeleted = false ORDER BY nt.usageCount DESC")
    List<NoteTemplate> findByCategoryOrderByUsageDesc(@Param("category") String category);

    /**
     * 查找系统模板
     */
    @Query("SELECT nt FROM NoteTemplate nt WHERE nt.isSystem = true AND nt.isDeleted = false ORDER BY nt.usageCount DESC")
    List<NoteTemplate> findSystemTemplates();

    /**
     * 查找用户自定义模板
     */
    @Query("SELECT nt FROM NoteTemplate nt WHERE nt.isSystem = false AND nt.isDeleted = false ORDER BY nt.usageCount DESC")
    List<NoteTemplate> findUserTemplates();

    /**
     * 搜索模板
     */
    @Query("SELECT nt FROM NoteTemplate nt WHERE nt.isDeleted = false AND " +
           "(nt.name LIKE %:keyword% OR nt.description LIKE %:keyword% OR nt.category LIKE %:keyword%)")
    Page<NoteTemplate> searchTemplates(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 获取所有分类
     */
    @Query("SELECT DISTINCT nt.category FROM NoteTemplate nt WHERE nt.isDeleted = false AND nt.category IS NOT NULL")
    List<String> findAllCategories();

    /**
     * 增加使用次数
     */
    @Modifying
    @Query("UPDATE NoteTemplate nt SET nt.usageCount = nt.usageCount + 1 WHERE nt.id = :id")
    void incrementUsageCount(@Param("id") Long id);
}