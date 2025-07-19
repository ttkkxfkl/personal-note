package com.personalnote.controller;

import com.personalnote.common.Result;
import com.personalnote.dto.ai.*;
import com.personalnote.service.AIService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * AI功能控制器
 * 提供各种AI能力的REST API接口
 */
@Api(tags = "AI智能助手")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIController {

    private final AIService aiService;

    @ApiOperation("AI写作助手 - 内容生成")
    @PostMapping("/writing/generate")
    public Result<AIWritingResponse> generateContent(@Valid @RequestBody AIWritingRequest request) {
        try {
            AIWritingResponse response = aiService.generateContent(request);
            return Result.success("内容生成成功", response);
        } catch (Exception e) {
            return Result.error("内容生成失败: " + e.getMessage());
        }
    }

    @ApiOperation("AI写作助手 - 内容续写")
    @PostMapping("/writing/continue")
    public Result<AIWritingResponse> continueWriting(
            @RequestParam String content,
            @RequestParam(required = false) String context) {
        try {
            AIWritingResponse response = aiService.continueWriting(content, context);
            return Result.success("内容续写成功", response);
        } catch (Exception e) {
            return Result.error("内容续写失败: " + e.getMessage());
        }
    }

    @ApiOperation("AI写作助手 - 内容改写")
    @PostMapping("/writing/rewrite")
    public Result<AIWritingResponse> rewriteContent(
            @RequestParam String content,
            @RequestParam String style) {
        try {
            AIWritingResponse response = aiService.rewriteContent(content, style);
            return Result.success("内容改写成功", response);
        } catch (Exception e) {
            return Result.error("内容改写失败: " + e.getMessage());
        }
    }

    @ApiOperation("智能摘要生成")
    @PostMapping("/summary")
    public Result<AISummaryResponse> generateSummary(
            @RequestParam String content,
            @RequestParam(defaultValue = "200") int maxLength) {
        try {
            AISummaryResponse response = aiService.generateSummary(content, maxLength);
            return Result.success("摘要生成成功", response);
        } catch (Exception e) {
            return Result.error("摘要生成失败: " + e.getMessage());
        }
    }

    @ApiOperation("关键词提取")
    @PostMapping("/keywords")
    public Result<List<String>> extractKeywords(
            @RequestParam String content,
            @RequestParam(defaultValue = "10") int maxCount) {
        try {
            List<String> keywords = aiService.extractKeywords(content, maxCount);
            return Result.success("关键词提取成功", keywords);
        } catch (Exception e) {
            return Result.error("关键词提取失败: " + e.getMessage());
        }
    }

    @ApiOperation("情感分析")
    @PostMapping("/sentiment")
    public Result<AISentimentResponse> analyzeSentiment(@RequestParam String content) {
        try {
            AISentimentResponse response = aiService.analyzeSentiment(content);
            return Result.success("情感分析成功", response);
        } catch (Exception e) {
            return Result.error("情感分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("智能问答")
    @PostMapping("/qa")
    public Result<AIQAResponse> askQuestion(
            @RequestParam String question,
            @RequestParam(required = false) String context) {
        try {
            AIQAResponse response = aiService.askQuestion(question, context);
            return Result.success("问答成功", response);
        } catch (Exception e) {
            return Result.error("问答失败: " + e.getMessage());
        }
    }

    @ApiOperation("语音转录")
    @PostMapping("/transcription")
    public Result<AITranscriptionResponse> transcribeAudio(@RequestParam("file") MultipartFile audioFile) {
        try {
            if (audioFile.isEmpty()) {
                return Result.error("音频文件不能为空");
            }
            AITranscriptionResponse response = aiService.transcribeAudio(audioFile);
            return Result.success("语音转录成功", response);
        } catch (Exception e) {
            return Result.error("语音转录失败: " + e.getMessage());
        }
    }

    @ApiOperation("OCR文字提取")
    @PostMapping("/ocr")
    public Result<AIOCRResponse> extractTextFromImage(@RequestParam("file") MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return Result.error("图片文件不能为空");
            }
            AIOCRResponse response = aiService.extractTextFromImage(imageFile);
            return Result.success("OCR提取成功", response);
        } catch (Exception e) {
            return Result.error("OCR提取失败: " + e.getMessage());
        }
    }

    @ApiOperation("图片内容描述")
    @PostMapping("/image/describe")
    public Result<AIImageDescriptionResponse> describeImage(@RequestParam("file") MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return Result.error("图片文件不能为空");
            }
            AIImageDescriptionResponse response = aiService.describeImage(imageFile);
            return Result.success("图片描述成功", response);
        } catch (Exception e) {
            return Result.error("图片描述失败: " + e.getMessage());
        }
    }

    @ApiOperation("文本翻译")
    @PostMapping("/translation")
    public Result<AITranslationResponse> translateText(
            @RequestParam String text,
            @RequestParam String fromLang,
            @RequestParam String toLang) {
        try {
            AITranslationResponse response = aiService.translateText(text, fromLang, toLang);
            return Result.success("翻译成功", response);
        } catch (Exception e) {
            return Result.error("翻译失败: " + e.getMessage());
        }
    }

    @ApiOperation("智能分类")
    @PostMapping("/classification")
    public Result<AIClassificationResponse> classifyContent(@RequestParam String content) {
        try {
            AIClassificationResponse response = aiService.classifyContent(content);
            return Result.success("分类成功", response);
        } catch (Exception e) {
            return Result.error("分类失败: " + e.getMessage());
        }
    }

    @ApiOperation("标签推荐")
    @PostMapping("/tags/recommend")
    public Result<List<String>> recommendTags(
            @RequestParam String content,
            @RequestParam(defaultValue = "5") int maxCount) {
        try {
            List<String> tags = aiService.recommendTags(content, maxCount);
            return Result.success("标签推荐成功", tags);
        } catch (Exception e) {
            return Result.error("标签推荐失败: " + e.getMessage());
        }
    }

    @ApiOperation("语法检查")
    @PostMapping("/grammar")
    public Result<AIGrammarCheckResponse> checkGrammar(@RequestParam String content) {
        try {
            AIGrammarCheckResponse response = aiService.checkGrammar(content);
            return Result.success("语法检查成功", response);
        } catch (Exception e) {
            return Result.error("语法检查失败: " + e.getMessage());
        }
    }

    @ApiOperation("写作建议")
    @PostMapping("/writing/suggestions")
    public Result<AIWritingSuggestionResponse> getWritingSuggestions(@RequestParam String content) {
        try {
            AIWritingSuggestionResponse response = aiService.getWritingSuggestions(content);
            return Result.success("写作建议生成成功", response);
        } catch (Exception e) {
            return Result.error("写作建议生成失败: " + e.getMessage());
        }
    }

    @ApiOperation("相似笔记检索")
    @PostMapping("/similarity")
    public Result<AISimilarityResponse> findSimilarNotes(
            @RequestParam String content,
            @RequestParam(required = false) Long excludeNoteId) {
        try {
            AISimilarityResponse response = aiService.findSimilarNotes(content, excludeNoteId);
            return Result.success("相似笔记检索成功", response);
        } catch (Exception e) {
            return Result.error("相似笔记检索失败: " + e.getMessage());
        }
    }

    @ApiOperation("AI聊天对话")
    @PostMapping("/chat")
    public Result<AIChatResponse> chat(
            @RequestParam String message,
            @RequestBody(required = false) List<AIChatMessage> chatHistory) {
        try {
            AIChatResponse response = aiService.chat(message, chatHistory);
            return Result.success("AI聊天成功", response);
        } catch (Exception e) {
            return Result.error("AI聊天失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量AI处理")
    @PostMapping("/batch")
    public Result<BatchAIResponse> batchProcess(@Valid @RequestBody BatchAIRequest request) {
        try {
            BatchAIResponse response = new BatchAIResponse();
            
            // 根据请求类型批量处理
            for (String content : request.getContents()) {
                switch (request.getProcessType()) {
                    case "summary":
                        response.addResult(aiService.generateSummary(content, request.getMaxLength()));
                        break;
                    case "keywords":
                        response.addResult(aiService.extractKeywords(content, request.getMaxCount()));
                        break;
                    case "sentiment":
                        response.addResult(aiService.analyzeSentiment(content));
                        break;
                    case "classification":
                        response.addResult(aiService.classifyContent(content));
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的处理类型: " + request.getProcessType());
                }
            }
            
            return Result.success("批量处理成功", response);
        } catch (Exception e) {
            return Result.error("批量处理失败: " + e.getMessage());
        }
    }

    // 批量AI请求DTO
    public static class BatchAIRequest {
        private List<String> contents;
        private String processType; // summary, keywords, sentiment, classification
        private Integer maxLength = 200;
        private Integer maxCount = 10;

        // getters and setters
        public List<String> getContents() { return contents; }
        public void setContents(List<String> contents) { this.contents = contents; }
        public String getProcessType() { return processType; }
        public void setProcessType(String processType) { this.processType = processType; }
        public Integer getMaxLength() { return maxLength; }
        public void setMaxLength(Integer maxLength) { this.maxLength = maxLength; }
        public Integer getMaxCount() { return maxCount; }
        public void setMaxCount(Integer maxCount) { this.maxCount = maxCount; }
    }

    // 批量AI响应DTO
    public static class BatchAIResponse {
        private List<Object> results = new java.util.ArrayList<>();
        private Integer totalProcessed = 0;
        private Integer successCount = 0;
        private Integer failureCount = 0;

        public void addResult(Object result) {
            results.add(result);
            totalProcessed++;
            if (result != null) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        // getters and setters
        public List<Object> getResults() { return results; }
        public void setResults(List<Object> results) { this.results = results; }
        public Integer getTotalProcessed() { return totalProcessed; }
        public void setTotalProcessed(Integer totalProcessed) { this.totalProcessed = totalProcessed; }
        public Integer getSuccessCount() { return successCount; }
        public void setSuccessCount(Integer successCount) { this.successCount = successCount; }
        public Integer getFailureCount() { return failureCount; }
        public void setFailureCount(Integer failureCount) { this.failureCount = failureCount; }
    }
}