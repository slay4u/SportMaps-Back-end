package sport_maps.nef.dto;

import java.time.LocalDateTime;

public record ForumSaveDto(String name, LocalDateTime date, String text, String author) {}
