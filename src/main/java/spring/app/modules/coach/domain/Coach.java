package spring.app.modules.coach.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.commons.domain.ImageData;
import spring.app.modules.commons.domain.SportType;
import spring.app.modules.commons.util.convert.BaseDto;

import java.util.List;

@Entity
@Table(name = "coaches")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coach")
    private Long idCoach;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "age", nullable = false, length = 2)
    private Long age;
    @Column(name = "experience", nullable = false, length = 2)
    private Long experience;
    @Column(name = "price", nullable = false, length = 10)
    private Double price;
    @Lob
    private String description;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "sport_type")
    private SportType sportType;
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ImageData> imageDataList;
}
