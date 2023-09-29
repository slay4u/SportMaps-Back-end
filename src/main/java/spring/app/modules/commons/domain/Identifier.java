package spring.app.modules.commons.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class Identifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private LocalDateTime lastUpdated;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
