package com.pi.qna.dto;

import javax.validation.constraints.NotBlank;

public record NewAnswerForm(
        @NotBlank String body,
        Long parentId) { }
