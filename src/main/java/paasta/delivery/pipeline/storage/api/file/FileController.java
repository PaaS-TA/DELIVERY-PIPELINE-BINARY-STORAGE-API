package paasta.delivery.pipeline.storage.api.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by hrjin on 2017-06-12.
 */
@RestController
@RequestMapping(value = "/file")
public class FileController {

    private final FileService fileService;

    /**
     * Instantiates a new File controller.
     *
     * @param fileService the file service
     */
    @Autowired
    public FileController(FileService fileService) {this.fileService = fileService;}


    /**
     * Upload file file info.
     *
     * @param multipartFile the multipart file
     * @return the file info
     * @throws IOException the io exception
     */
    @RequestMapping(value = {"/uploadFile"}, method = RequestMethod.POST)
    @ResponseBody
    public FileInfo uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return fileService.upload(multipartFile);
    }


    /**
     * Delete file string.
     *
     * @param fileInfo the file info
     * @return the string
     */
    @RequestMapping(value = {"/fileDelete"}, method = RequestMethod.POST)
    @ResponseBody
    public String deleteFile(@RequestBody FileInfo fileInfo) {
        return fileService.deleteFile(fileInfo);
    }

}
