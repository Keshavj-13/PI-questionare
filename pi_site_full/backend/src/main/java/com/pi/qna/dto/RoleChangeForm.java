package com.pi.qna.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleChangeForm(@NotBlank String role) { }
