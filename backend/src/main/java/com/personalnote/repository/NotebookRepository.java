package com.personalnote.repository;

import com.personalnote.entity.Notebook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long> {

    /**
     * 查找未删除的笔记本列表
     */
    @Query("SELECT n FROM Notebook n WHERE n.isDeleted = false ORDER BY n.sortOrder ASC, n.createdTime DESC")
    List<Notebook> findAllActive();

    /**
     * 分页查找未删除的笔记本
     */
    @Query("SELECT n FROM Notebook n WHERE n.isDeleted = false")
    Page<Notebook> findAllActive(Pageable pageable);

    /**
     * 根据ID查找未删除的笔记本
     */
    @Query("SELECT n FROM Notebook n WHERE n.id = :id AND n.isDeleted = false")
    Optional<Notebook> findActiveById(@Param("id") Long id);

    /**
     * 根据名称查找未删除的笔记本
     */
    @Query("SELECT n FROM Notebook n WHERE n.name = :name AND n.isDeleted = false")
    Optional<Notebook> findActiveByName(@Param("name") String name);

    /**
     * 搜索笔记本
     */
    @Query("SELECT n FROM Notebook n WHERE n.isDeleted = false AND " +
           "(n.name LIKE %:keyword% OR n.description LIKE %:keyword%)")
    Page<Notebook> searchNotebooks(@Param("keyword") String keyword, Pageable pageable);
}