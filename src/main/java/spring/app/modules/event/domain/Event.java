package spring.app.modules.event.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.commons.domain.ImageData;
import spring.app.modules.commons.domain.SportType;
import spring.app.modules.commons.util.convert.ConvertType;
import spring.app.modules.commons.util.convert.Dto;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "name", nullable = false, unique = true, length = 50)
    @Dto
    private String name;

    @Column(name = "event_date", nullable = false, length = 50)
    @Dto
    private LocalDateTime eventDate;

    @Lob
    @Dto(property = "desc")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "sport_type")
    @Dto(value = ConvertType.STRING)
    private SportType sportType;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ImageData> imageDataList;
}
