package com.pi.qna.controller;

import com.pi.qna.dto.TopicDto;
import com.pi.qna.dto.NewTopicForm;
import com.pi.qna.entity.Topic;
import com.pi.qna.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public List<TopicDto> list() {
        return topicService.listAll()
                .stream()
                .map(t -> new TopicDto(t.getId(), t.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TopicDto create(@RequestBody @Valid NewTopicForm form) {
        Topic t = topicService.create(form.name());
        return new TopicDto(t.getId(), t.getName());
    }
}
