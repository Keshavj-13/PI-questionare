package com.pi.qna.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "answers",
        indexes = {
                @Index(name = "idx_answer_question", columnList = "question_id"),
                @Index(name = "idx_answer_parent",   columnList = "parent_id")
        }
)
@Getter @Setter @NoArgsConstructor
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /* ------------ relations ------------ */

    /* the question this answer belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    /* author (must be TEACHER) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /* self‑reference for threaded replies */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Answer parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> replies;
}
