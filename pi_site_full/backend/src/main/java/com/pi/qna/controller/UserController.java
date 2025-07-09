package com.pi.qna.controller;

import com.pi.qna.dto.UserDto;
import com.pi.qna.entity.User;
import com.pi.qna.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());   // ← extract "sub" claim

        User user = userService.findById(userId);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRole().name());
    }

    @GetMapping("/{id}")
    public UserDto one(@PathVariable Long id) {
        User user = userService.findById(id);
        return new UserDto(user.getId(), user.getUsername(), user.getRole().name());
    }
}
