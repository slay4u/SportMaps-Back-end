package spring.app.modules.comments.forumComment.service;

import spring.app.modules.comments.forumComment.domain.ForumComment;
import spring.app.modules.comments.forumComment.dto.ForumCommentDto;

import java.util.List;
import java.util.stream.Collectors;

public interface ForumCommentGeneralHandler {
    default ForumCommentDto allInfoDto(ForumComment forumComment) {
        return ForumCommentDto.builder()
                .id(forumComment.getId())
                .createdDate(String.valueOf(forumComment.getCreatedDate()))
                .text(forumComment.getText())
                .emailUser(forumComment.getCreatedBy().getEmail())
                .idForum(forumComment.getForum().getIdForum())
                .build();
    }

    default List<ForumCommentDto> listToDto(List<ForumComment> comments) {
        return comments.stream().map(this::allInfoDto).collect(Collectors.toList());
    }
}
