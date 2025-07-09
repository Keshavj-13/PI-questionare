package com.pi.qna.dto;

import javax.validation.constraints.NotBlank;

public record NewTopicForm(@NotBlank String name) { }
