package spring.app.modules.news.service;

import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.news.dto.NewAllInfoDto;
import spring.app.modules.news.dto.NewCreateDto;

import java.io.IOException;
import java.util.List;

public interface NewService {
    int createNew(NewCreateDto aNew);

    int updateNew(Long id, NewCreateDto aNew);

    NewAllInfoDto getNewById(Long id);

    void deleteById(Long id);

    List<NewAllInfoDto> getAllNews(int pageNumber);

    String uploadImage(MultipartFile file, Long id) throws IOException;

    double getTotalPagesCount();
}
