package paasta.delivery.pipeline.storage.api.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * paastaDeliveryPipelineApi
 * paasta.delivery.pipeline.storage.api.common
 *
 * @author REX
 * @version 1.0
 * @since 6 / 7/2017
 */
@Aspect
@Service
public class AspectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectService.class);


    /**
     * On before log service access.
     *
     * @param joinPoint the join point
     */
    @Before("execution(* paasta.delivery..*Service.*(..))")
    public void onBeforeLogServiceAccess(JoinPoint joinPoint) {
        LOGGER.warn("######## ON BEFORE SERVICE ACCESS :: {}", joinPoint);
    }


    /**
     * On before log controller access.
     *
     * @param joinPoint the join point
     */
    @Before("execution(public * paasta.delivery..*Controller.*(..))")
    public void onBeforeLogControllerAccess(JoinPoint joinPoint) {
        LOGGER.warn("#### ON BEFORE CONTROLLER ACCESS :: {}", joinPoint);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        LOGGER.warn("## Entering in Method:  {}", joinPoint.getSignature().getName());
        LOGGER.warn("## Class Name:  {}", joinPoint.getSignature().getDeclaringTypeName());
        LOGGER.warn("## Arguments:  {}", Arrays.toString(joinPoint.getArgs()));
        LOGGER.warn("## Target class:  {}", joinPoint.getTarget().getClass().getName());

        if (null != request) {
            LOGGER.warn("## Request Path info:  {}", request.getServletPath());
            LOGGER.warn("## Method Type:  {}", request.getMethod());
            LOGGER.warn("================================================================================");
            LOGGER.warn("Start Header Section of request");
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                LOGGER.warn("  Header Name: {} || Header Value: {}", headerName, headerValue);
            }
            LOGGER.warn("End Header Section of request");
            LOGGER.warn("================================================================================");
        }
    }

}
