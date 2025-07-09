package com.pi.qna.dto;

import javax.validation.constraints.NotBlank;

public record RoleChangeForm(@NotBlank String role) { }
