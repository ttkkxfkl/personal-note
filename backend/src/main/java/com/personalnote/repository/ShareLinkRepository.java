package com.personalnote.repository;

import com.personalnote.entity.ShareLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {

    /**
     * 根据分享令牌查找链接
     */
    @Query("SELECT sl FROM ShareLink sl WHERE sl.shareToken = :shareToken AND sl.isDeleted = false")
    Optional<ShareLink> findByShareToken(@Param("shareToken") String shareToken);

    /**
     * 根据笔记ID查找分享链接
     */
    @Query("SELECT sl FROM ShareLink sl WHERE sl.note.id = :noteId AND sl.isDeleted = false ORDER BY sl.createdTime DESC")
    List<ShareLink> findByNoteId(@Param("noteId") Long noteId);

    /**
     * 查找活跃的分享链接
     */
    @Query("SELECT sl FROM ShareLink sl WHERE sl.note.id = :noteId AND sl.isActive = true AND sl.isDeleted = false " +
           "AND (sl.expireTime IS NULL OR sl.expireTime > :currentTime)")
    List<ShareLink> findActiveByNoteId(@Param("noteId") Long noteId, @Param("currentTime") LocalDateTime currentTime);

    /**
     * 查找已过期的分享链接
     */
    @Query("SELECT sl FROM ShareLink sl WHERE sl.expireTime IS NOT NULL AND sl.expireTime <= :currentTime AND sl.isActive = true AND sl.isDeleted = false")
    List<ShareLink> findExpiredLinks(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 分页查找所有分享链接
     */
    @Query("SELECT sl FROM ShareLink sl WHERE sl.isDeleted = false ORDER BY sl.createdTime DESC")
    Page<ShareLink> findAllActive(Pageable pageable);

    /**
     * 统计笔记的分享链接数量
     */
    @Query("SELECT COUNT(sl) FROM ShareLink sl WHERE sl.note.id = :noteId AND sl.isDeleted = false")
    Long countByNoteId(@Param("noteId") Long noteId);

    /**
     * 增加访问次数
     */
    @Modifying
    @Query("UPDATE ShareLink sl SET sl.viewCount = sl.viewCount + 1, sl.lastAccessedTime = :accessTime WHERE sl.id = :id")
    void incrementViewCount(@Param("id") Long id, @Param("accessTime") LocalDateTime accessTime);

    /**
     * 批量禁用过期链接
     */
    @Modifying
    @Query("UPDATE ShareLink sl SET sl.isActive = false WHERE sl.expireTime IS NOT NULL AND sl.expireTime <= :currentTime AND sl.isActive = true")
    int disableExpiredLinks(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 根据创建者查找分享链接
     */
    @Query("SELECT sl FROM ShareLink sl WHERE sl.createdBy = :createdBy AND sl.isDeleted = false ORDER BY sl.createdTime DESC")
    Page<ShareLink> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);
}