package spring.app.modules.forum.service;

import spring.app.modules.forum.domain.Forum;
import spring.app.modules.forum.dto.ForumAllInfoDto;

import java.util.List;
import java.util.stream.Collectors;

public interface ForumGeneralHandler {
    default ForumAllInfoDto allInfoDto(Forum forum) {
        return ForumAllInfoDto.builder()
                .id(forum.getIdForum())
                .name(forum.getName())
                .createDate(String.valueOf(forum.getCreateDate()))
                .desc(forum.getDescription())
                .emailUser(forum.getCreatedBy().getEmail())
                .build();
    }

    default List<ForumAllInfoDto> listToDto(List<Forum> forums) {
        return forums.stream().map(this::allInfoDto).collect(Collectors.toList());
    }
}
