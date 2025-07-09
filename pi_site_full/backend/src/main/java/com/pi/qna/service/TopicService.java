package com.pi.qna.service;

import com.pi.qna.entity.Topic;
import com.pi.qna.repository.TopicRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepo;

    public List<Topic> listAll() { return topicRepo.findAll(); }

    @Transactional
    public Topic create(String name) {
        return topicRepo.save(new Topic(null, name));
    }
}
