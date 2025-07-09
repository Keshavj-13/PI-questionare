package com.pi.qna.controller;

import com.pi.qna.dto.NewUserForm;
import com.pi.qna.dto.UserDto;
import com.pi.qna.dto.TokenResponse;
import com.pi.qna.entity.User;
import com.pi.qna.service.UserService;
import com.pi.qna.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenService jwtService;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid NewUserForm form) {
        User user = userService.register(form.username(), form.password());
        return new UserDto(user.getId(), user.getUsername(), user.getRole().name());
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid NewUserForm form) {
        User user = userService.login(form.username(), form.password());
        return new TokenResponse(jwtService.issueToken(user));
    }
}
