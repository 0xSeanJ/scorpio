package top.jshanet.scorpio.framework.web.mvc.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.jshanet.scorpio.framework.autoconfig.ScorpioWebMvcAsyncProperties;
import top.jshanet.scorpio.framework.core.deferred.DeferredRunnable;
import top.jshanet.scorpio.framework.core.deferred.ServiceExecutor;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.service.RequestNoService;
import top.jshanet.scorpio.framework.status.ScorpioStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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

    protected DeferredResult<?> execute(ServiceExecutor<?> serviceExecutor) {
        ScorpioResponse timeoutResponse = new ScorpioResponse(ScorpioStatus.DefaultStatus.WEB_TIMEOUT);
        DeferredResult<?> deferredResult = new DeferredResult<>(scorpioWebMvcAsyncProperties.getTimeout(), timeoutResponse);
        try {
            String requestNo = requestNoService.nextRequestNo();
            DeferredRunnable<?, ?> deferredRunnable = new DeferredRunnable(
                    requestNo, deferredResult, serviceExecutor,
                    isEnableRestMessage());
            threadPoolTaskExecutor.submit(deferredRunnable);

        } catch (Exception ignored) {
        }
        return deferredResult;
    }

    protected <T> DeferredResult<T> executeT(ServiceExecutor<T> serviceExecutor) {
        return (DeferredResult<T>) execute(serviceExecutor);
    }


}
