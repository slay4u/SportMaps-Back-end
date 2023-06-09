package spring.app.modules.forum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "publish_date", nullable = false, length = 50)
    private LocalDateTime createDate;

    @Lob
    private String description;
}
