package sport_maps.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {
    Path IMG_DIR = Path.of("src/main/resources/images");
    void uploadImage(String title, MultipartFile file) throws IOException;
    void updateImage(Long id, MultipartFile file) throws IOException;
    void deleteAllImages(Long id) throws IOException;
}
