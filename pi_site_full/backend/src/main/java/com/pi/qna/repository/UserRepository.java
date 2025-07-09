package com.pi.qna.repository;

import com.pi.qna.entity.Role;
import com.pi.qna.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // find by username (used in login)
    Optional<User> findByUsername(String username);

    // list all users by role (e.g., to show all teachers)
    List<User> findByRole(Role role);
}
