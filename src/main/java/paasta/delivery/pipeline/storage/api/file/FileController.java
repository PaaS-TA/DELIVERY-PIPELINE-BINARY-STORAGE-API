package paasta.delivery.pipeline.storage.api.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by hrjin on 2017-06-12.
 */
@RestController
@RequestMapping
public class FileController {

    private static  final Logger logger = LoggerFactory.getLogger(FileController.class);
    private static final String BASE_URL = "/file";

    @Autowired
    private FileService fileService;


    /**
     *  파일 업로드 - execute
     *
     * @param multipartFile
     * @return file upload
     * @throws Exception
     */
    @RequestMapping(value = {BASE_URL + "/uploadFile"}, method = RequestMethod.POST)
    @ResponseBody
    public FileInfo uploadFile(@RequestParam("file") MultipartFile multipartFile) throws Exception{
        logger.info("uploadFile :: param:: {}", multipartFile.toString());
        System.out.println("originalName:::"+multipartFile.getOriginalFilename());
        return fileService.upload(multipartFile);
    }




    /**
     * 업로드된 파일 삭제 - execute
     *
     * @param fileInfo
     * @return
     */
    @RequestMapping(value = {BASE_URL + "/fileDelete"}, method = RequestMethod.POST)
    @ResponseBody
    public String deleteFile(@RequestBody FileInfo fileInfo){
        // TODO
        fileService.deleteFile(fileInfo);
        return "delete success";
    }
}
