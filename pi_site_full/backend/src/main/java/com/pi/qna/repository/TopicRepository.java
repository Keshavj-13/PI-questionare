package com.pi.qna.repository;

import com.pi.qna.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    // optional: find a topic by name
    Optional<Topic> findByName(String name);
}