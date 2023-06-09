package spring.app.modules.forum.service;

import spring.app.modules.forum.dto.ForumAllInfoDto;
import spring.app.modules.forum.dto.ForumCreateDto;

import java.util.List;

public interface ForumService {
    int createForum(ForumCreateDto forumCreateDto);

    int updateForum(Long id, ForumCreateDto forumCreateDto);

    ForumAllInfoDto getForumById(Long id);

    void deleteById(Long id);

    List<ForumAllInfoDto> getAllForums(int pageNumber);

    double getTotalPagesCount();
}
