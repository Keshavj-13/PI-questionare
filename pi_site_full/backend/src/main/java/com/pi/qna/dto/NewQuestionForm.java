package com.pi.qna.dto;

import javax.validation.constraints.NotBlank;

public record NewQuestionForm(
        @NotBlank String title,
        @NotBlank String content,
        Long topicId) { }
