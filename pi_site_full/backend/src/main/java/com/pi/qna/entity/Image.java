package com.pi.qna.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter @Setter @NoArgsConstructor
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* store a URL (recommended) */
    @Column(nullable = false, length = 512)
    private String url;

    /* or swap for raw bytes:
       @Lob
       @Column(name = "data", columnDefinition = "LONGBLOB", nullable = false)
       private byte[] data;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
