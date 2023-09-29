package spring.app.modules.news.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class New {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_new")
    private Long idNew;

    @Column(name = "name", nullable = false, unique = true, length = 130)
    private String name;

    @Column(name = "publish_date", nullable = false)
    private LocalDateTime publishDate;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User createdBy;
}
