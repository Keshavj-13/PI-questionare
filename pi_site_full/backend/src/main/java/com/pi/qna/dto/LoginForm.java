package com.pi.qna.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
    @NotBlank String username,
    @NotBlank String password) { }
