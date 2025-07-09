package com.pi.qna.dto;

import jakarta.validation.constraints.NotBlank;

public record NewUserForm(
    @NotBlank String username,
    @NotBlank String password) { }
