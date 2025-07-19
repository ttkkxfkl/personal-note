package com.personalnote.service;

import com.personalnote.dto.AttachmentDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    /**
     * 上传文件
     */
    AttachmentDTO uploadFile(MultipartFile file, Long noteId) throws IOException;

    /**
     * 上传图片并生成缩略图
     */
    AttachmentDTO uploadImage(MultipartFile file, Long noteId) throws IOException;

    /**
     * 下载文件
     */
    Resource downloadFile(Long attachmentId) throws IOException;

    /**
     * 删除文件
     */
    void deleteFile(Long attachmentId) throws IOException;

    /**
     * 获取文件信息
     */
    AttachmentDTO getFileInfo(Long attachmentId);

    /**
     * 根据笔记ID获取所有附件
     */
    List<AttachmentDTO> getAttachmentsByNoteId(Long noteId);

    /**
     * 根据笔记ID获取所有图片
     */
    List<AttachmentDTO> getImagesByNoteId(Long noteId);

    /**
     * 生成图片缩略图
     */
    String generateThumbnail(String imagePath) throws IOException;

    /**
     * 获取文件访问URL
     */
    String getFileUrl(String filename);

    /**
     * 检查文件类型是否允许
     */
    boolean isAllowedFileType(String mimeType);

    /**
     * 检查文件大小是否超限
     */
    boolean isFileSizeValid(long fileSize);
}