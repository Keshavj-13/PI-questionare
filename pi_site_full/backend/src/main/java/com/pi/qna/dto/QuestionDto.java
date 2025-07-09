package com.pi.qna.dto;

import java.time.LocalDateTime;
import java.util.List;

public record QuestionDto(
        Long id,
        String title,
        String content,
        String author,
        String topic,
        List<String> imageUrls,
        LocalDateTime createdAt) { }
