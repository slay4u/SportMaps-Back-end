package sport_maps.nef.dto;

import sport_maps.comments.dto.CommentDto;
import java.util.List;

public record EventDto(Long id, String name, String date, String text, String author,
                       String sportType, byte[] image, List<CommentDto> comments) {}
