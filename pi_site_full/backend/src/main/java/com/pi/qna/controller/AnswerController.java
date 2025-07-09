package com.pi.qna.controller;

import com.pi.qna.dto.AnswerDto;
import com.pi.qna.dto.NewAnswerForm;
import com.pi.qna.entity.Answer;
import com.pi.qna.service.AnswerService;
import com.pi.qna.service.MappingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions/{qId}/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final MappingUtil mapper;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public AnswerDto add(@PathVariable Long qId,
                         @Valid @RequestBody NewAnswerForm form,
                         @AuthenticationPrincipal JwtUser user) {

        Answer a = (form.parentId() == null)
                ? answerService.answerQuestion(user.id(), qId, form.body())
                : answerService.replyToAnswer(user.id(), form.parentId(), form.body());

        return mapper.toDto(a);
    }

    @GetMapping
    public List<AnswerDto> roots(@PathVariable Long qId) {
        return answerService.rootAnswers(qId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{answerId}/replies")
    public List<AnswerDto> replies(@PathVariable Long answerId) {
        return answerService.replies(answerId).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
