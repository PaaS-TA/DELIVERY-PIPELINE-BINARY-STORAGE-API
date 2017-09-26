package paasta.delivery.pipeline.storage.api.file;

import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import paasta.delivery.pipeline.storage.api.common.Constants;
import paasta.delivery.pipeline.storage.api.common.RestTemplateService;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by hrjin on 2017-06-12.
 */
@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private final RestTemplateService restTemplateService;

    @Autowired
    Container container;

    @Autowired
    public FileService(RestTemplateService restTemplateService) {this.restTemplateService = restTemplateService;}

    /**
     * MultipartFile 을 업로드 ::: Swift Object Storage
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    FileInfo upload(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String storedFileName = uuid + originalFileName.substring(originalFileName.lastIndexOf('.'));
        StoredObject object = container.getObject(storedFileName);

        LOGGER.info("object ::: {}", object);
        LOGGER.info("url ::: {}", object.getPublicURL());

        object.uploadObject(multipartFile.getInputStream());

        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setStoredFileName(storedFileName);
        fileInfo.setFileUrl(object.getPublicURL());

        FileInfo resultModel = restTemplateService.send(Constants.TARGET_COMMON_API, "/file/upload", HttpMethod.POST, fileInfo, FileInfo.class);
        resultModel.setResultStatus(Constants.RESULT_STATUS_SUCCESS);

        return resultModel;
    }


    /**
     * 파일 삭제
     *
     * @param deleteFile
     * @return
     */
    public String deleteFile(FileInfo deleteFile) {

        String storedFileName = deleteFile.getStoredFileName();
        StoredObject object = container.getObject(storedFileName);
        LOGGER.info("object ::: " + object);
        object.delete();

        return "delete success.";
    }

}
