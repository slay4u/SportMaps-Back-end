package spring.app.modules.coach.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.Length;
import spring.app.modules.commons.domain.Identifier;
import spring.app.modules.commons.util.convert.BaseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"first_name", "last_name", "dob"})
})
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@BaseDto(exclude = {"id"})
public class Coach extends Identifier {
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 2)
    private LocalDate dob;
    // TODO:
    @Column(nullable = false, length = 10)
    private BigDecimal price;
    @Column(length = Length.LONG32)
    private String description;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "type_exp_mapping",
            joinColumns = {@JoinColumn(name = "coach_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "sport_type")
    @Column(name = "experience", nullable = false)
    private Map<String, Long> sports;
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ImageData> imageDataList;
}
