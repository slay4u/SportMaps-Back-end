package spring.app.modules.comments.forumComment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.forum.domain.Forum;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "forum_comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_forum_comment")
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_forum", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Forum forum;
}
