package paasta.delivery.pipeline.storage.api.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * paastaDeliveryPipelineApi
 * paasta.delivery.pipeline.storage.api.common
 *
 * @author REX
 * @version 1.0
 * @since 6 /21/2017
 */
@Service
public class RestTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateService.class);
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private final RestTemplate restTemplate;
    private String base64Authorization;
    private String baseUrl;

    // COMMON API
    @Value("${commonApi.url}")
    private String commonApiUrl;

    @Value("${commonApi.authorization.id}")
    private String commonApiAuthorizationId;

    @Value("${commonApi.authorization.password}")
    private String commonApiAuthorizationPassword;


    /**
     * Instantiates a new Rest template service.
     *
     * @param restTemplate the rest template
     */
    @Autowired
    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Send t.
     *
     * @param <T>          the type parameter
     * @param reqApi       the req api
     * @param reqUrl       the req url
     * @param httpMethod   the http method
     * @param bodyObject   the body object
     * @param responseType the response type
     * @return the t
     */
    public <T> T send(String reqApi, String reqUrl, HttpMethod httpMethod, Object bodyObject, Class<T> responseType) {

        setApiUrlAuthorization(reqApi);

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        reqHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> reqEntity = new HttpEntity<>(bodyObject, reqHeaders);

        LOGGER.info("<T> T send :: Request : {} {baseUrl} : {}, Content-Type: {}", httpMethod, reqUrl, reqHeaders.get(CONTENT_TYPE));
        ResponseEntity<T> resEntity = restTemplate.exchange(baseUrl + reqUrl, httpMethod, reqEntity, responseType);
        LOGGER.info("Response Type: {}", resEntity.getBody().getClass());

        return resEntity.getBody();
    }


    private void setApiUrlAuthorization(String reqApi) {

        String apiUrl = "";
        String authorization = "";

        // COMMON API
        if (Constants.TARGET_COMMON_API.equals(reqApi)) {
            apiUrl = commonApiUrl;
            authorization = commonApiAuthorizationId + ":" + commonApiAuthorizationPassword;
        }

        this.base64Authorization = "Basic " + Base64Utils.encodeToString(authorization.getBytes(StandardCharsets.UTF_8));
        this.baseUrl = apiUrl;
    }

}
