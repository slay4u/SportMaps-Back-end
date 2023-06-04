package spring.app.modules.smap.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SMap extends SPosition {

    private float zoom;
    private Type mapType;
    @OneToMany(mappedBy = "sMap", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SMarker> sMarkers;

    public enum Type {
        ROADMAP(0),
        SATELLITE(1),
        TERRAIN(2),
        HYBRID(3);

        private final Integer code;

        Type(int code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }
}
