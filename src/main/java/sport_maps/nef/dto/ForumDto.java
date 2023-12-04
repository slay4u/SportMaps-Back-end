package sport_maps.nef.dto;

import sport_maps.comments.dto.CommentDto;
import java.util.List;

public record ForumDto(Long id, String name, String date, String text, String author, List<CommentDto> comments) {}
