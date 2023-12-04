package sport_maps.nef.dto;

import sport_maps.comments.dto.CommentDto;
import java.util.List;

public record NewsDto(Long id, String name, String date, String text, String author,
                      byte[] image, List<CommentDto> comments) {}
