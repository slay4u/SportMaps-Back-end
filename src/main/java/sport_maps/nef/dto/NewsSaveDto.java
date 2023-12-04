package sport_maps.nef.dto;

import java.time.LocalDateTime;

public record NewsSaveDto(String name, LocalDateTime date, String text, String author) {}
