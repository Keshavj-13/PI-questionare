package com.pi.qna.dto;

import java.time.LocalDateTime;

public record AnswerDto(
        Long id,
        String body,
        String author,
        Long parentId,
        LocalDateTime createdAt) { }
