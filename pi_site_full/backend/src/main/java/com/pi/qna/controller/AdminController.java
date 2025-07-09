package com.pi.qna.controller;

import com.pi.qna.dto.RoleChangeForm;
import com.pi.qna.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PatchMapping("/users/{id}/role")
    public void changeRole(@PathVariable Long id,
                           @RequestBody RoleChangeForm form) {
        userService.changeRole(id, form.role());
    }
}
