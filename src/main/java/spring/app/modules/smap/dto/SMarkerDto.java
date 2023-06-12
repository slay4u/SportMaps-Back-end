package spring.app.modules.smap.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class SMarkerDto {

    private String title;
    private String label;
    private Position position;
    private String description;

    public static class Position {
        @NotNull
        public float lat;
        @NotNull
        public float lng;

        public Position(float lat, float lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public Position() {
        }
    }
}
