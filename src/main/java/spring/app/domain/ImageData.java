package spring.app.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "id_new")
    @JsonBackReference
    private New aNew;

    @ManyToOne
    @JoinColumn(name = "id_event")
    @JsonBackReference
    private Event event;

    public ImageData(Long id, String name, String type, String filePath, New aNew) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.filePath = filePath;
        this.aNew = aNew;
    }

    public ImageData(Long id, String name, String type, String filePath, Event event) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.filePath = filePath;
        this.event = event;
    }
}
