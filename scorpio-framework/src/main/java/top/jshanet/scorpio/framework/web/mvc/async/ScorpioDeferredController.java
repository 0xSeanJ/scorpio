package top.jshanet.scorpio.framework.web.mvc.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.jshanet.scorpio.framework.autoconfig.ScorpioWebMvcAsyncProperties;
import top.jshanet.scorpio.framework.core.deferred.DeferredExceptionResolver;
import top.jshanet.scorpio.framework.core.deferred.DeferredRunnable;
import top.jshanet.scorpio.framework.core.deferred.ServiceExecutor;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.service.RequestNoService;
import top.jshanet.scorpio.framework.status.ScorpioStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author seanjiang
 * @since 2020-07-13
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class ScorpioDeferredController {


    @Autowired
    @Qualifier("webTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ScorpioWebMvcAsyncProperties scorpioWebMvcAsyncProperties;

    @Autowired
    private RequestNoService requestNoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private DeferredExceptionResolver exceptionResolver;

    private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache =
            new LinkedHashMap<>();


    protected HandlerMethod getHandlerMethod() throws Exception {
        return (HandlerMethod) handlerMapping.getHandler(httpServletRequest).getHandler();
    }

    protected Method getRequestMethod() throws Exception {
        return getHandlerMethod().getMethod();
    }

    protected Class getRequestClass() throws Exception {
        return getHandlerMethod().getBeanType();
    }


    private boolean isEnableRestMessage() throws Exception {
        Method method = getRequestMethod();
        Class clazz = getRequestClass();
        return method.isAnnotationPresent(ScorpioRestMessage.class)
                || clazz.isAnnotationPresent(ScorpioRestMessage.class)
                || scorpioWebMvcAsyncProperties.isEnableGlobalRestMessage();
    }


    protected DeferredResult<?> execute(ServiceExecutor<?> serviceExecutor, Long timeout) {
        ScorpioResponse timeoutResponse = ScorpioResponse.fromStatus(ScorpioStatus.DefaultStatus.WEB_TIMEOUT);
        DeferredResult<?> deferredResult = new DeferredResult<>(
                timeout == null ? scorpioWebMvcAsyncProperties.getTimeout() : timeout,
                timeoutResponse);
        try {
            String requestNo = requestNoService.nextRequestNo();
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);
            DeferredRunnable<?, ?> deferredRunnable = new DeferredRunnable(
                    requestNo, deferredResult, serviceExecutor,
                    isEnableRestMessage(), httpServletRequest, httpServletResponse, exceptionResolver);
            // support spring security
            DelegatingSecurityContextAsyncTaskExecutor securityContextAsyncTaskExecutor =
                    new DelegatingSecurityContextAsyncTaskExecutor(threadPoolTaskExecutor, SecurityContextHolder.getContext());
            securityContextAsyncTaskExecutor.submit(deferredRunnable);
        } catch (Exception ignored) {
        }
        return deferredResult;
    }

    protected DeferredResult<?> execute(ServiceExecutor<?> serviceExecutor) {
        return execute(serviceExecutor, null);
    }

    protected <T> DeferredResult<T> executeT(ServiceExecutor<T> serviceExecutor, Long timeout) {
        return (DeferredResult<T>) execute(serviceExecutor, timeout);
    }

    protected <T> DeferredResult<T> executeT(ServiceExecutor<T> serviceExecutor) {
        return (DeferredResult<T>) execute(serviceExecutor);
    }


}
