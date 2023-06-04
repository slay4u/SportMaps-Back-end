package spring.app.modules.smap.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SMarker extends SPosition {

    @ManyToOne
    @JoinColumn(name = "id_smap")
    private SMap sMap;
    private String label;
    private String title;
    private boolean draggable;
    @Enumerated(EnumType.ORDINAL)
    private Animation animation;
    private String iconPath;

    private enum Animation {
        BOUNCE, DROP
    }
}
