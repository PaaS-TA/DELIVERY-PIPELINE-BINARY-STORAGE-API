package paasta.delivery.pipeline.storage.api.file;

import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import paasta.delivery.pipeline.storage.api.common.Constants;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by hrjin on 2017-06-12.
 */
@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    private final Container container;

    /**
     * Instantiates a new File service.
     *
     * @param container the container
     */
    @Autowired
    public FileService(Container container) {this.container = container;}


    /**
     * Upload file info.
     *
     * @param multipartFile the multipart file
     * @return the file info
     * @throws IOException the io exception
     */
    FileInfo upload(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String storedFileName = uuid + originalFileName.substring(originalFileName.lastIndexOf('.'));
        StoredObject object = container.getObject(storedFileName);

        LOGGER.info("UPLOAD :: object ::: {}", object);
        LOGGER.info("UPLOAD :: url ::: {}", object.getPublicURL());

        object.uploadObject(multipartFile.getInputStream());

        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setStoredFileName(storedFileName);
        fileInfo.setFileUrl(object.getPublicURL());
        fileInfo.setResultStatus(Constants.RESULT_STATUS_SUCCESS);

        return fileInfo;
    }


    /**
     * Delete file.
     *
     * @param deleteFile the delete file
     */
    String deleteFile(FileInfo deleteFile) {

        String storedFileName = deleteFile.getStoredFileName();
        StoredObject object = container.getObject(storedFileName);

        LOGGER.info("DELETE :: storedFileName ::: {}", storedFileName);
        LOGGER.info("DELETE :: object ::: {}", object);

        object.delete();

        return Constants.RESULT_STATUS_SUCCESS;
    }
}
