package sport_maps.nef.service;

import org.springframework.web.multipart.MultipartFile;
import sport_maps.nef.dto.NewsDto;
import sport_maps.nef.dto.NewsSaveDto;

import java.io.IOException;
import java.util.List;

public interface NewsService {
    int createNew(NewsSaveDto dto);
    int updateNew(Long id, NewsSaveDto dto);
    NewsDto getNewById(Long id);
    void deleteById(Long id);
    List<NewsDto> getAllNews(int pageNumber);
    String uploadImage(MultipartFile file, Long id) throws IOException;
    double getTotalPagesCount();
}
