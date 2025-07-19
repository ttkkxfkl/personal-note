package com.personalnote.service;

import com.personalnote.dto.ai.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * AI服务接口
 * 提供各种AI能力的统一接口
 */
public interface AIService {

    /**
     * AI写作助手 - 内容生成
     */
    AIWritingResponse generateContent(AIWritingRequest request);

    /**
     * AI写作助手 - 内容续写
     */
    AIWritingResponse continueWriting(String content, String context);

    /**
     * AI写作助手 - 内容改写
     */
    AIWritingResponse rewriteContent(String content, String style);

    /**
     * 智能摘要生成
     */
    AISummaryResponse generateSummary(String content, int maxLength);

    /**
     * 内容分析 - 关键词提取
     */
    List<String> extractKeywords(String content, int maxCount);

    /**
     * 内容分析 - 情感分析
     */
    AISentimentResponse analyzeSentiment(String content);

    /**
     * 智能问答
     */
    AIQAResponse askQuestion(String question, String context);

    /**
     * 语音转录
     */
    AITranscriptionResponse transcribeAudio(MultipartFile audioFile);

    /**
     * 图片OCR文字提取
     */
    AIOCRResponse extractTextFromImage(MultipartFile imageFile);

    /**
     * 图片内容描述
     */
    AIImageDescriptionResponse describeImage(MultipartFile imageFile);

    /**
     * 文本翻译
     */
    AITranslationResponse translateText(String text, String fromLang, String toLang);

    /**
     * 智能分类推荐
     */
    AIClassificationResponse classifyContent(String content);

    /**
     * 标签推荐
     */
    List<String> recommendTags(String content, int maxCount);

    /**
     * 语法检查
     */
    AIGrammarCheckResponse checkGrammar(String content);

    /**
     * 写作建议
     */
    AIWritingSuggestionResponse getWritingSuggestions(String content);

    /**
     * 内容相似度检测
     */
    AISimilarityResponse findSimilarNotes(String content, Long excludeNoteId);

    /**
     * AI聊天对话
     */
    AIChatResponse chat(String message, List<AIChatMessage> chatHistory);
}