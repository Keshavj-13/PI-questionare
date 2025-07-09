package com.pi.qna.service;

import com.pi.qna.entity.Question;
import com.pi.qna.entity.Topic;
import com.pi.qna.entity.User;
import com.pi.qna.repository.QuestionRepository;
import com.pi.qna.repository.TopicRepository;
import com.pi.qna.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;
    private final TopicRepository topicRepo;

    /** Ask a new question (images handled later) */
    @Transactional
    public Question ask(long userId, String title, String content, Long topicId) {
        User author = userRepo.getReferenceById(userId);
        Topic topic = (topicId != null) ? topicRepo.getReferenceById(topicId) : null;

        Question q = new Question();
        q.setAuthor(author);
        q.setTitle(title);
        q.setContent(content);
        q.setCreatedAt(LocalDateTime.now());
        q.setTopic(topic);
        return questionRepo.save(q);
    }
    @Transactional(readOnly = true)
    public Page<Question> listByTopic(Long topicId, Pageable pageable) {
        return questionRepo.findByTopicId(topicId, pageable);
    }



    public Question get(long id) {
        return questionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
    }

    public List<Question> listByTopic(Long topicId) {
        Topic t = topicRepo.getReferenceById(topicId);
        return questionRepo.findByTopic(t);
    }
}
