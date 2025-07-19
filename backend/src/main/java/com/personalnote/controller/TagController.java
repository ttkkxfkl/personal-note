package com.personalnote.controller;

import com.personalnote.common.Result;
import com.personalnote.dto.TagDTO;
import com.personalnote.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "标签管理")
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TagController {

    private final TagService tagService;

    @ApiOperation("获取所有标签")
    @GetMapping("/all")
    public Result<List<TagDTO>> getAllTags() {
        try {
            List<TagDTO> tags = tagService.getAllTags();
            return Result.success(tags);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}