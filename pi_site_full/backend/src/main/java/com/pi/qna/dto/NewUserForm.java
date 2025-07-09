package com.pi.qna.dto;

import javax.validation.constraints.NotBlank;

public record NewUserForm(
    @NotBlank String username,
    @NotBlank String password) { }
