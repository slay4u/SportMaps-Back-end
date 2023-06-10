package spring.app.modules.forum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "forums")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_forum")
    private Long idForum;

    @Column(name = "name", nullable = false, unique = true, length = 60)
    private String name;

    @Column(name = "publish_date", nullable = false)
    private LocalDateTime createDate;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User createdBy;
}
