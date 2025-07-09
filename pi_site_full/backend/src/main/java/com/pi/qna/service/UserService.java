package com.pi.qna.service;

import com.pi.qna.entity.*;
import com.pi.qna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder; // BCrypt bean

    /** register locally (for Google OAuth you'd handle tokens instead) */
    @Transactional
    public User register(String username, String rawPassword) {
        if (userRepo.findByUsername(username).isPresent())
            throw new IllegalStateException("Username taken");

        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setRole(Role.STUDENT);
        return userRepo.save(u);
    }

    public User findById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /** admin can promote/demote */
    @Transactional
    public void changeRole(long userId, Role role) {
        User u = findById(userId);
        u.setRole(role);
    }

    @Transactional(readOnly = true)
    public User login(String username, String rawPassword) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return user;
    }
}
