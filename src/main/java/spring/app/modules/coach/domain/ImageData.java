package spring.app.modules.coach.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.imgs.domain.BaseImageData;

@Entity
@Table(name = "img_coach")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageData extends BaseImageData {

    @ManyToOne
    @JoinColumn(name = "id_coach")
    private Coach coach;
}
