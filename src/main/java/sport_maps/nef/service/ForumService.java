package sport_maps.nef.service;

import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;

import java.util.List;

public interface ForumService {
    int createForum(ForumSaveDto dto);
    int updateForum(Long id, ForumSaveDto dto);
    ForumDto getForumById(Long id);
    void deleteById(Long id);
    List<ForumDto> getAllForums(int pageNumber);
    double getTotalPagesCount();
}
