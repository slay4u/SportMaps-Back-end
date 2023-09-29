package spring.app.modules.imgs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Do not query this table directly
 */
@Entity
@Table(name = "img")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String name;
    private String type;
    private String filePath;
}
