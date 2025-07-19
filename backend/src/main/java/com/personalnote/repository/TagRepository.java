package com.personalnote.repository;

import com.personalnote.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 查找未删除的标签列表
     */
    @Query("SELECT t FROM Tag t WHERE t.isDeleted = false ORDER BY t.name ASC")
    List<Tag> findAllActive();

    /**
     * 分页查找未删除的标签
     */
    @Query("SELECT t FROM Tag t WHERE t.isDeleted = false")
    Page<Tag> findAllActive(Pageable pageable);

    /**
     * 根据ID查找未删除的标签
     */
    @Query("SELECT t FROM Tag t WHERE t.id = :id AND t.isDeleted = false")
    Optional<Tag> findActiveById(@Param("id") Long id);

    /**
     * 根据名称查找未删除的标签
     */
    @Query("SELECT t FROM Tag t WHERE t.name = :name AND t.isDeleted = false")
    Optional<Tag> findActiveByName(@Param("name") String name);

    /**
     * 根据名称列表查找标签
     */
    @Query("SELECT t FROM Tag t WHERE t.name IN :names AND t.isDeleted = false")
    List<Tag> findActiveByNames(@Param("names") List<String> names);

    /**
     * 搜索标签
     */
    @Query("SELECT t FROM Tag t WHERE t.isDeleted = false AND t.name LIKE %:keyword%")
    Page<Tag> searchTags(@Param("keyword") String keyword, Pageable pageable);
}