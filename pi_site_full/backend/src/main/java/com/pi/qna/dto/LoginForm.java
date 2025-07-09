package com.pi.qna.dto;

import javax.validation.constraints.NotBlank;

public record LoginForm(
    @NotBlank String username,
    @NotBlank String password) { }
