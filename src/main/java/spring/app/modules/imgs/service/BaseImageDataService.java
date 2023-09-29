package spring.app.modules.imgs.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.coach.service.CoachServiceImpl;
import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.exception.InternalProcessingException;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.commons.service.BaseService;
import spring.app.modules.imgs.dao.BaseImageDataDao;
import spring.app.modules.imgs.domain.BaseImageData;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Service for base image handling operations.
 *
 * @author Ivan Krylosov
 */
public abstract class BaseImageDataService<IMG, ID, DAO extends BaseImageDataDao<IMG, ID>> extends BaseService<IMG, ID, DAO> {

    private final DAO imageDataDao;

    // Path to image folder
    private final String FULL_IMG_PATH;

    protected BaseImageDataService(DAO dao) {
        super(dao);
        this.imageDataDao = dao;
        this.FULL_IMG_PATH = getIMG_PATH();
    }

    private String getIMG_PATH() {
        URL res = CoachServiceImpl.class.getClassLoader().getResource(SystemConstants.IMG_FOLDER_NAME);
        assert res != null;
        File file;
        try {
            file = Paths.get(res.toURI()).toFile();
        } catch (Exception e) {
            throw new InternalProcessingException("Unable to get folder path the URL.");
        }
        return file.getAbsolutePath();
    }

    public int uploadImage(MultipartFile file, IMG img) {
        String filePath = FULL_IMG_PATH + "\\" + file.getOriginalFilename();

        Consumer<IMG> daoAction = entity -> {
            try {
                file.transferTo(new File(filePath));
            } catch (IOException e) {
                throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
            }
            imageDataDao.save(entity);
        };

        return baseSave(img, daoAction);
    }

    public byte[] downloadImagesZip(ID id) {
        List<BaseImageData> images = getImages(id);
        if (images.isEmpty()) {
            return null;
        }
        byte[] res;
        try {
            res = zipImages(images.stream().map(BaseImageData::getFilePath).toList());
        } catch (IOException e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        }
        return res;
    }

    private byte[] zipImages(List<String> imgPathList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        List<File> fileList = new ArrayList<>();

        for (String imgPath : imgPathList) {
            fileList.add(new File(imgPath));
        }

        for (File file : fileList) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new NotFoundException(SystemConstants.NOT_FOUND_ERROR + file.getPath());
            }

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        zipOutputStream.finish();
        zipOutputStream.flush();
        IOUtils.closeQuietly(zipOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] downloadImage(ID id) {
        List<BaseImageData> images = getImages(id);
        if (images.isEmpty()) {
            return null;
        }
        // TODO: change
        BaseImageData singleImage = images.stream().findFirst().orElseThrow();
        String imagePath = singleImage.getFilePath();
        byte[] res;
        try {
            res = Files.readAllBytes(new File(imagePath).toPath());
        } catch (IOException e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private List<BaseImageData> getImages(ID id) {
        return (List<BaseImageData>) imageDataDao.findAllByFK(id);
    }
}
