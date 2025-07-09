package com.pi.qna.controller;

import com.pi.qna.dto.NewQuestionForm;
import com.pi.qna.dto.QuestionDto;
import com.pi.qna.entity.Question;
import com.pi.qna.service.MappingUtil;
import com.pi.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.Valid;
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
                           @AuthenticationPrincipal JwtUser user) {
        Question q = questionService.ask(user.id(), form.title(), form.content(), form.topicId());
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
}
