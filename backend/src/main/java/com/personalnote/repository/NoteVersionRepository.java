package com.personalnote.repository;

import com.personalnote.entity.NoteVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteVersionRepository extends JpaRepository<NoteVersion, Long> {

    /**
     * 根据笔记ID查找版本历史
     */
    @Query("SELECT nv FROM NoteVersion nv WHERE nv.note.id = :noteId AND nv.isDeleted = false ORDER BY nv.versionNumber DESC")
    List<NoteVersion> findByNoteIdOrderByVersionDesc(@Param("noteId") Long noteId);

    /**
     * 分页查找笔记版本历史
     */
    @Query("SELECT nv FROM NoteVersion nv WHERE nv.note.id = :noteId AND nv.isDeleted = false")
    Page<NoteVersion> findByNoteId(@Param("noteId") Long noteId, Pageable pageable);

    /**
     * 查找笔记的最新版本号
     */
    @Query("SELECT COALESCE(MAX(nv.versionNumber), 0) FROM NoteVersion nv WHERE nv.note.id = :noteId AND nv.isDeleted = false")
    Integer findMaxVersionByNoteId(@Param("noteId") Long noteId);

    /**
     * 根据笔记ID和版本号查找特定版本
     */
    @Query("SELECT nv FROM NoteVersion nv WHERE nv.note.id = :noteId AND nv.versionNumber = :versionNumber AND nv.isDeleted = false")
    Optional<NoteVersion> findByNoteIdAndVersionNumber(@Param("noteId") Long noteId, @Param("versionNumber") Integer versionNumber);

    /**
     * 统计笔记的版本数量
     */
    @Query("SELECT COUNT(nv) FROM NoteVersion nv WHERE nv.note.id = :noteId AND nv.isDeleted = false")
    Long countByNoteId(@Param("noteId") Long noteId);
}