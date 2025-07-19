package com.personalnote.repository;

import com.personalnote.entity.Attachment;
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
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    /**
     * 根据笔记ID查找附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.note.id = :noteId AND a.isDeleted = false ORDER BY a.createdTime DESC")
    List<Attachment> findByNoteId(@Param("noteId") Long noteId);

    /**
     * 根据笔记ID分页查找附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.note.id = :noteId AND a.isDeleted = false")
    Page<Attachment> findByNoteId(@Param("noteId") Long noteId, Pageable pageable);

    /**
     * 根据文件类型查找附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.fileType = :fileType AND a.isDeleted = false ORDER BY a.createdTime DESC")
    List<Attachment> findByFileType(@Param("fileType") String fileType);

    /**
     * 查找图片附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.isImage = true AND a.isDeleted = false ORDER BY a.createdTime DESC")
    List<Attachment> findImageAttachments();

    /**
     * 根据笔记ID查找图片附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.note.id = :noteId AND a.isImage = true AND a.isDeleted = false ORDER BY a.createdTime DESC")
    List<Attachment> findImagesByNoteId(@Param("noteId") Long noteId);

    /**
     * 统计笔记的附件数量
     */
    @Query("SELECT COUNT(a) FROM Attachment a WHERE a.note.id = :noteId AND a.isDeleted = false")
    Long countByNoteId(@Param("noteId") Long noteId);

    /**
     * 统计附件总大小
     */
    @Query("SELECT COALESCE(SUM(a.fileSize), 0) FROM Attachment a WHERE a.isDeleted = false")
    Long getTotalFileSize();

    /**
     * 根据文件名查找附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.filename = :filename AND a.isDeleted = false")
    Optional<Attachment> findByFilename(@Param("filename") String filename);

    /**
     * 增加下载次数
     */
    @Modifying
    @Query("UPDATE Attachment a SET a.downloadCount = a.downloadCount + 1 WHERE a.id = :id")
    void incrementDownloadCount(@Param("id") Long id);

    /**
     * 搜索附件
     */
    @Query("SELECT a FROM Attachment a WHERE a.isDeleted = false AND " +
           "(a.originalFilename LIKE %:keyword% OR a.altText LIKE %:keyword%)")
    Page<Attachment> searchAttachments(@Param("keyword") String keyword, Pageable pageable);
}