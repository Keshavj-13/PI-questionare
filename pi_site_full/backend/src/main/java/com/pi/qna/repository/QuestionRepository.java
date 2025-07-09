package com.pi.qna.repository;

import com.pi.qna.entity.Question;
import com.pi.qna.entity.User;
import com.pi.qna.entity.Topic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // All questions by a specific user
    List<Question> findByAuthor(User author);

    // All questions by topic
    List<Question> findByTopic(Topic topic);

    // Optional: search by title/content (case-insensitive)
    List<Question> findByTitleContainingIgnoreCase(String keyword);
}
