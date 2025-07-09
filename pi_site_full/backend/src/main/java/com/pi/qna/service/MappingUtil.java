package com.pi.qna.service;

import com.pi.qna.dto.AnswerDto;
import com.pi.qna.dto.QuestionDto;
import com.pi.qna.entity.Answer;
import com.pi.qna.entity.Image;
import com.pi.qna.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class MappingUtil {

    public QuestionDto toDto(Question q) {
        return new QuestionDto(
                q.getId(), q.getTitle(), q.getContent(),
                q.getAuthor().getUsername(),
                (q.getTopic() == null ? null : q.getTopic().getName()),
                q.getImages().stream().map(Image::getUrl).toList(),
                q.getCreatedAt());
    }

    public AnswerDto toDto(Answer a) {
        return new AnswerDto(a.getId(), a.getBody(),
                a.getAuthor().getUsername(),
                a.getParent() == null ? null : a.getParent().getId(),
                a.getCreatedAt());
    }
}
