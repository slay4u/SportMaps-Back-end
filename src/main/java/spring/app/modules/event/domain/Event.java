package spring.app.modules.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.pd.st.SportType;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long idEvent;

    @Column(name = "name", nullable = false, unique = true, length = 130)
    private String name;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Lob
    private String description;

    @NotNull
    @Column(name = "sport_type", nullable = false)
    private SportType sportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User createdBy;
}
