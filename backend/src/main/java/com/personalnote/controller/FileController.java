package com.personalnote.controller;

import com.personalnote.common.Result;
import com.personalnote.dto.AttachmentDTO;
import com.personalnote.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "文件管理")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {

    private final FileService fileService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result<AttachmentDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("noteId") Long noteId) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            if (!fileService.isAllowedFileType(file.getContentType())) {
                return Result.error("不支持的文件类型");
            }
            
            if (!fileService.isFileSizeValid(file.getSize())) {
                return Result.error("文件大小超出限制");
            }
            
            AttachmentDTO attachment = fileService.uploadFile(file, noteId);
            return Result.success("文件上传成功", attachment);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("上传图片")
    @PostMapping("/upload/image")
    public Result<AttachmentDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("noteId") Long noteId) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            if (!file.getContentType().startsWith("image/")) {
                return Result.error("只支持图片文件");
            }
            
            AttachmentDTO attachment = fileService.uploadImage(file, noteId);
            return Result.success("图片上传成功", attachment);
        } catch (IOException e) {
            return Result.error("图片上传失败: " + e.getMessage());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("下载文件")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            AttachmentDTO attachment = fileService.getFileInfo(id);
            if (attachment == null) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = fileService.downloadFile(id);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(attachment.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename=\"" + attachment.getOriginalFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("获取文件信息")
    @GetMapping("/{id}")
    public Result<AttachmentDTO> getFileInfo(@PathVariable Long id) {
        try {
            AttachmentDTO attachment = fileService.getFileInfo(id);
            return Result.success(attachment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/{id}")
    public Result<String> deleteFile(@PathVariable Long id) {
        try {
            fileService.deleteFile(id);
            return Result.success("文件删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("获取笔记的所有附件")
    @GetMapping("/note/{noteId}")
    public Result<List<AttachmentDTO>> getAttachmentsByNoteId(@PathVariable Long noteId) {
        try {
            List<AttachmentDTO> attachments = fileService.getAttachmentsByNoteId(noteId);
            return Result.success(attachments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("获取笔记的所有图片")
    @GetMapping("/note/{noteId}/images")
    public Result<List<AttachmentDTO>> getImagesByNoteId(@PathVariable Long noteId) {
        try {
            List<AttachmentDTO> images = fileService.getImagesByNoteId(noteId);
            return Result.success(images);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}