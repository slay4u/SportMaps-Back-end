package sport_maps.comments.dto;

public record CommentDto(Long id, String date, String text, String createdBy, Long idEntity) {
}
