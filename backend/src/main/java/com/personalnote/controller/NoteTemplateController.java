package com.personalnote.controller;

import com.personalnote.common.PageResult;
import com.personalnote.common.Result;
import com.personalnote.dto.NoteTemplateDTO;
import com.personalnote.service.NoteTemplateService;
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

@Api(tags = "笔记模板管理")
@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NoteTemplateController {

    private final NoteTemplateService noteTemplateService;

    @ApiOperation("创建模板")
    @PostMapping
    public Result<NoteTemplateDTO> createTemplate(@Valid @RequestBody NoteTemplateDTO templateDTO) {
        try {
            NoteTemplateDTO createdTemplate = noteTemplateService.createTemplate(templateDTO);
            return Result.success("模板创建成功", createdTemplate);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("更新模板")
    @PutMapping("/{id}")
    public Result<NoteTemplateDTO> updateTemplate(@PathVariable Long id, @Valid @RequestBody NoteTemplateDTO templateDTO) {
        try {
            NoteTemplateDTO updatedTemplate = noteTemplateService.updateTemplate(id, templateDTO);
            return Result.success("模板更新成功", updatedTemplate);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/{id}")
    public Result<String> deleteTemplate(@PathVariable Long id) {
        try {
            noteTemplateService.deleteTemplate(id);
            return Result.success("模板删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("根据ID获取模板")
    @GetMapping("/{id}")
    public Result<NoteTemplateDTO> getTemplateById(@PathVariable Long id) {
        try {
            NoteTemplateDTO template = noteTemplateService.getTemplateById(id);
            return Result.success(template);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("获取所有模板")
    @GetMapping("/all")
    public Result<List<NoteTemplateDTO>> getAllTemplates() {
        try {
            List<NoteTemplateDTO> templates = noteTemplateService.getAllTemplates();
            return Result.success(templates);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("分页获取模板")
    @GetMapping
    public Result<PageResult<NoteTemplateDTO>> getTemplates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "usageCount") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        try {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            
            Page<NoteTemplateDTO> templates = noteTemplateService.getTemplates(pageable);
            return Result.success(PageResult.of(templates));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("根据分类获取模板")
    @GetMapping("/category/{category}")
    public Result<List<NoteTemplateDTO>> getTemplatesByCategory(@PathVariable String category) {
        try {
            List<NoteTemplateDTO> templates = noteTemplateService.getTemplatesByCategory(category);
            return Result.success(templates);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("获取系统模板")
    @GetMapping("/system")
    public Result<List<NoteTemplateDTO>> getSystemTemplates() {
        try {
            List<NoteTemplateDTO> templates = noteTemplateService.getSystemTemplates();
            return Result.success(templates);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("获取用户自定义模板")
    @GetMapping("/user")
    public Result<List<NoteTemplateDTO>> getUserTemplates() {
        try {
            List<NoteTemplateDTO> templates = noteTemplateService.getUserTemplates();
            return Result.success(templates);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("搜索模板")
    @GetMapping("/search")
    public Result<PageResult<NoteTemplateDTO>> searchTemplates(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "usageCount"));
            Page<NoteTemplateDTO> templates = noteTemplateService.searchTemplates(keyword, pageable);
            return Result.success(PageResult.of(templates));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("获取所有分类")
    @GetMapping("/categories")
    public Result<List<String>> getAllCategories() {
        try {
            List<String> categories = noteTemplateService.getAllCategories();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("使用模板")
    @PostMapping("/{id}/use")
    public Result<String> useTemplate(@PathVariable Long id) {
        try {
            noteTemplateService.useTemplate(id);
            return Result.success("模板使用成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("初始化系统模板")
    @PostMapping("/initialize")
    public Result<String> initializeSystemTemplates() {
        try {
            noteTemplateService.initializeSystemTemplates();
            return Result.success("系统模板初始化成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}