package sport_maps.comments.dto;

import java.time.LocalDateTime;

public record CommentSaveDto(String text, String author, Long id, LocalDateTime date) {}
