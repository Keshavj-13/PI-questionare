package com.pi.qna.dto;

import jakarta.validation.constraints.NotBlank;

public record NewAnswerForm(
        @NotBlank String body,
        Long parentId) { }
