package com.pi.qna.dto;

import jakarta.validation.constraints.NotBlank;

public record NewTopicForm(@NotBlank String name) { }
