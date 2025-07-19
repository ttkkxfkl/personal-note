package com.personalnote.controller;

import com.personalnote.common.PageResult;
import com.personalnote.common.Result;
import com.personalnote.dto.NotebookDTO;
import com.personalnote.service.NotebookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "笔记本管理")
@RestController
@RequestMapping("/notebooks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotebookController {

    private final NotebookService notebookService;

    @ApiOperation("创建笔记本")
    @PostMapping
    public Result<NotebookDTO> createNotebook(@Valid @RequestBody NotebookDTO notebookDTO) {
        try {
            NotebookDTO createdNotebook = notebookService.createNotebook(notebookDTO);
            return Result.success("笔记本创建成功", createdNotebook);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("更新笔记本")
    @PutMapping("/{id}")
    public Result<NotebookDTO> updateNotebook(@PathVariable Long id, @Valid @RequestBody NotebookDTO notebookDTO) {
        try {
            NotebookDTO updatedNotebook = notebookService.updateNotebook(id, notebookDTO);
            return Result.success("笔记本更新成功", updatedNotebook);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("删除笔记本")
    @DeleteMapping("/{id}")
    public Result<String> deleteNotebook(@PathVariable Long id) {
        try {
            notebookService.deleteNotebook(id);
            return Result.success("笔记本删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("根据ID获取笔记本")
    @GetMapping("/{id}")
    public Result<NotebookDTO> getNotebookById(@PathVariable Long id) {
        try {
            NotebookDTO notebook = notebookService.getNotebookById(id);
            return Result.success(notebook);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("获取所有笔记本")
    @GetMapping("/all")
    public Result<List<NotebookDTO>> getAllNotebooks() {
        try {
            List<NotebookDTO> notebooks = notebookService.getAllNotebooks();
            return Result.success(notebooks);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("分页获取笔记本")
    @GetMapping
    public Result<PageResult<NotebookDTO>> getNotebooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedTime") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        try {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            
            Page<NotebookDTO> notebooks = notebookService.getNotebooks(pageable);
            return Result.success(PageResult.of(notebooks));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("搜索笔记本")
    @GetMapping("/search")
    public Result<PageResult<NotebookDTO>> searchNotebooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedTime"));
            Page<NotebookDTO> notebooks = notebookService.searchNotebooks(keyword, pageable);
            return Result.success(PageResult.of(notebooks));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}