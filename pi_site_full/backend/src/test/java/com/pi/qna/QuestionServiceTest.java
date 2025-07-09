package com.pi.qna;

import com.pi.qna.entity.Question;
import com.pi.qna.entity.User;
import com.pi.qna.service.QuestionService;
import com.pi.qna.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QuestionServiceTest {

  @Autowired private UserService userService;
  @Autowired private QuestionService questionService;

  @Test
  void askAndFetchQuestion() {
    User u = userService.register("dave", "pwd");
    Question q = questionService.ask(u.getId(), "How to test?", "Body", null);

    Question fetched = questionService.get(q.getId());
    assertEquals("How to test?", fetched.getTitle());
  }

  @Test
  void paginationWorks() {
    User u = userService.register("eva", "pwd");

    for (int i = 0; i < 15; i++)
      questionService.ask(u.getId(), "Q" + i, "Body", null);

    Page<Question> page0 = questionService.listByTopic(null, PageRequest.of(0, 10));
    Page<Question> page1 = questionService.listByTopic(null, PageRequest.of(1, 10));

    assertEquals(10, page0.getNumberOfElements());
    assertEquals(5, page1.getNumberOfElements());
  }
}
