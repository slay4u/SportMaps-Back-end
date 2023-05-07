package spring.app.service;

import org.springframework.web.multipart.MultipartFile;
import spring.app.dto.news.NewAllInfoDto;
import spring.app.dto.news.NewCreateDto;
import spring.app.dto.news.NewInfoDto;

import java.io.IOException;
import java.util.List;

public interface NewService {
    NewInfoDto createNew(NewCreateDto aNew);

    NewInfoDto updateNew(Long id, NewCreateDto aNew);

    NewAllInfoDto getNewById(Long id);

    void deleteById(Long id);

    List<NewAllInfoDto> getAllNews(int pageNumber);

    String uploadImage(MultipartFile file, Long id) throws IOException;

    byte[] downloadImages(Long id) throws IOException;

    double getTotalPagesCount();
}
