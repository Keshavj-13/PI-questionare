package com.pi.qna.controller;

import com.pi.qna.dto.NewQuestionForm;
import com.pi.qna.dto.QuestionDto;
import com.pi.qna.entity.Question;
import com.pi.qna.service.MappingUtil;
import com.pi.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final MappingUtil mapper;


    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    public QuestionDto ask(@Valid @RequestBody NewQuestionForm form,
                           @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());          // ← extract ID from token

        Question q = questionService.ask(
                userId,                                // ← use this instead of user.id()
                form.title(),
                form.content(),
                form.topicId());

        return mapper.toDto(q);
    }


    @GetMapping("/{id}")
    public QuestionDto get(@PathVariable Long id) {
        return mapper.toDto(questionService.get(id));
    }

    @GetMapping("/topic/{topicId}")
    public List<QuestionDto> byTopic(@PathVariable Long topicId) {
        return questionService.listByTopic(topicId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/topic/{id}")
    public Page<QuestionDto> byTopic(@PathVariable Long id,
                                     @PageableDefault(size = 10) Pageable pageable) {
        return questionService.listByTopic(id, pageable).map(mapper::toDto);
    }

}
