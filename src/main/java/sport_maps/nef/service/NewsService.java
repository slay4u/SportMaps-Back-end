package sport_maps.nef.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.nef.dto.NewsDto;
import sport_maps.nef.dto.NewsSaveDto;

import java.io.IOException;

public interface NewsService {
    int size = 15;
    void createNews(NewsSaveDto dto);
    void updateNews(Long id, NewsSaveDto dto);
    NewsDto getNewsById(Long id);
    void deleteById(Long id);
    Page<NewsDto> getAllNews(int page);
    String uploadImage(MultipartFile file, Long id) throws IOException;
}
