package com.pi.qna.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = @Index(name = "uk_username", columnList = "username", unique = true)
)
@Getter @Setter @NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;          // store a hash

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role = Role.STUDENT; // default

    /* Questions posted by this user */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    /* Answers written by this user */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    /* Convenience */
    public boolean isTeacher() { return role == Role.TEACHER; }
}
