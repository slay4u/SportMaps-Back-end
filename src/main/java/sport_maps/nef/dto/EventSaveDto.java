package sport_maps.nef.dto;

import java.time.LocalDateTime;

public record EventSaveDto(String name, LocalDateTime date, String text, String author, String sportType) {}
