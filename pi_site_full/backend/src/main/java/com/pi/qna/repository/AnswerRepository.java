package com.pi.qna.repository;

import com.pi.qna.entity.Answer;
import com.pi.qna.entity.Question;
import com.pi.qna.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Get all top-level answers for a question (parent == null)
    List<Answer> findByQuestionAndParentIsNull(Question question);

    // Get all replies to a specific comment
    List<Answer> findByParent(Answer parent);

    // All answers by a teacher
    List<Answer> findByAuthor(User author);

}
