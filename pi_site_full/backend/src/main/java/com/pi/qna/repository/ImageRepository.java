package com.pi.qna.repository;

import com.pi.qna.entity.Image;
import com.pi.qna.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // Get all images for a question
    List<Image> findByQuestion(Question question);
}
