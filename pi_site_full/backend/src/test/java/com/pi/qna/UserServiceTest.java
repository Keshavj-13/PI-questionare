package com.pi.qna;

import com.pi.qna.entity.User;
import com.pi.qna.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

  @Autowired private UserService userService;

  @Test
  void registerAndLoginSuccess() {
    User u = userService.register("alice", "secret");
    assertNotNull(u.getId());

    User logged = userService.login("alice", "secret");
    assertEquals("alice", logged.getUsername());
  }

  @Test
  void duplicateUsernameThrows() {
    userService.register("bob", "123");
    assertThrows(IllegalStateException.class,
        () -> userService.register("bob", "456"));
  }

  @Test
  void loginBadPasswordThrows() {
    userService.register("charlie", "pass");
    assertThrows(BadCredentialsException.class,
        () -> userService.login("charlie", "wrong"));
  }
}
