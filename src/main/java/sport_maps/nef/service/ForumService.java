package sport_maps.nef.service;

import org.springframework.data.domain.Page;
import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;

public interface ForumService {
    int size = 15;
    void createForum(ForumSaveDto dto);
    void updateForum(Long id, ForumSaveDto dto);
    ForumDto getForumById(Long id);
    void deleteById(Long id);
    Page<ForumDto> getAllForums(int page);
}
