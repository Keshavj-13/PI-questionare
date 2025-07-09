package com.pi.qna.service;

import com.pi.qna.entity.Answer;
import com.pi.qna.entity.Question;
import com.pi.qna.entity.User;
import com.pi.qna.repository.AnswerRepository;
import com.pi.qna.repository.QuestionRepository;
import com.pi.qna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepo;
    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;

    /* ========== WRITE OPERATIONS (teachers only) ========== */

    /** post a top‑level answer */
    @Transactional
    public Answer answerQuestion(long teacherId, long questionId, String body) {
        User teacher = verifyTeacher(teacherId);
        Question q   = questionRepo.getReferenceById(questionId);

        Answer a = new Answer();
        a.setAuthor(teacher);
        a.setQuestion(q);
        a.setBody(body);
        a.setCreatedAt(LocalDateTime.now());
        return answerRepo.save(a);
    }

    /** reply to an existing answer */
    @Transactional
    public Answer replyToAnswer(long teacherId, long parentAnswerId, String body) {
        User teacher = verifyTeacher(teacherId);
        Answer parent = answerRepo.getReferenceById(parentAnswerId);

        Answer reply = new Answer();
        reply.setAuthor(teacher);
        reply.setQuestion(parent.getQuestion());
        reply.setParent(parent);
        reply.setBody(body);
        reply.setCreatedAt(LocalDateTime.now());
        return answerRepo.save(reply);
    }

    /* ========== READ OPERATIONS ========== */

    /** all top‑level answers for a question */
    @Transactional(readOnly = true)
    public List<Answer> rootAnswers(Long questionId) {
        Question q = questionRepo.getReferenceById(questionId);
        return answerRepo.findByQuestionAndParentIsNull(q);
    }

    /** direct replies to a given answer */
    @Transactional(readOnly = true)
    public List<Answer> replies(Long parentAnswerId) {
        Answer parent = answerRepo.getReferenceById(parentAnswerId);
        return answerRepo.findByParent(parent);
    }

    /* ---------- helper ---------- */

    private User verifyTeacher(long id) {
        User u = userRepo.getReferenceById(id);
        if (!u.isTeacher())
            throw new IllegalStateException("Only teachers can post answers");
        return u;
    }
}
