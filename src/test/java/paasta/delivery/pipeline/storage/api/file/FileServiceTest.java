package paasta.delivery.pipeline.storage.api.file;

import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import paasta.delivery.pipeline.storage.api.common.Constants;

import java.io.File;
import java.io.FileInputStream;

/**
 * deliveryPipelineApi
 * paasta.delivery.pipeline.storage.api.file
 *
 * @author REX
 * @version 1.0
 * @since 10/30/2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileServiceTest {

    @Autowired
    private FileService fileService;


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////// MethodName_StateUnderTest_ExpectedBehavior
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void upload_delete_ValidModel_ReturnModel() throws Exception {
        File file = new File("./src/main/resources/logback-spring.xml");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));


        // TEST :; UPLOAD FILE
        FileInfo resultModel = fileService.upload(multipartFile);

        Assert.assertNotNull(resultModel.getOriginalFileName());
        Assert.assertNotNull(resultModel.getFileUrl());
        Assert.assertNotNull(resultModel.getOriginalFileName());
        Assert.assertEquals(Constants.RESULT_STATUS_SUCCESS, resultModel.getResultStatus());


        FileInfo testDeleteFile = new FileInfo();
        testDeleteFile.setStoredFileName(resultModel.getStoredFileName());


        // TEST :; DELETE FILE
        String result = fileService.deleteFile(testDeleteFile);

        Assert.assertEquals(result, Constants.RESULT_STATUS_SUCCESS);
    }

}
