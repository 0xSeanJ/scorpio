package top.jshanet.scorpio.framework.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;
import top.jshanet.scorpio.framework.common.constant.ScorpioStatus;
import top.jshanet.scorpio.framework.common.dto.ScorpioBaseMessage;
import top.jshanet.scorpio.framework.common.dto.ScorpioRestMessage;
import top.jshanet.scorpio.framework.common.exception.ScorpioException;
import top.jshanet.scorpio.framework.common.service.ScorpioServiceExecutor;
import top.jshanet.scorpio.framework.common.util.JsonMapper;
import top.jshanet.scorpio.framework.common.util.ScorpioContextUtils;
import top.jshanet.scorpio.framework.common.util.SeqUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author seanjiang
 * @date 2019/12/26
 */
@Log4j2
@SuppressWarnings("unchecked")
public abstract class ScorpioBaseController {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private long defaultTimeout = 10_000;

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    protected ThreadPoolTaskExecutor getFrontTaskExecutor() {
        return threadPoolTaskExecutor;
    }


    private Method getRequestMethod(HttpServletRequest servletRequest) throws Exception {
        HandlerExecutionChain handler =
                handlerMapping.getHandler(servletRequest);
        HandlerMethod hm = (HandlerMethod) handler.getHandler();
        return hm.getMethod();
    }

    private String getRequestUri(HttpServletRequest servletRequest) {
        return URL_PATH_HELPER.getRequestUri(servletRequest);
    }

    private Locale getRequestLocale(HttpServletRequest request) {
        Locale locale = (Locale) WebUtils.getSessionAttribute(request,
                SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);

        if (locale == null) {
            locale = request.getLocale();
        }

        if (locale == null) {
            locale = Locale.CHINESE;
        }

        return locale;
    }

    private class DeferredResultRunnable<E, T extends ScorpioBaseMessage> implements Runnable {

        private String bizSeqNo;
        private DeferredResult<T> deferredResult;
        private HttpServletRequest servletRequest;
        private HttpServletResponse servletResponse;
        private ScorpioServiceExecutor<E, T> serviceExecutor;
        private E request;

        private Method method;
        private String requestUri;
        private Locale locale;

        public DeferredResultRunnable(String bizSeqNo, DeferredResult<T> deferredResult,
                                      HttpServletRequest servletRequest,
                                      HttpServletResponse servletResponse,
                                      Method method,
                                      E request,
                                      ScorpioServiceExecutor<E, T> serviceExecutor) {
            this.bizSeqNo = bizSeqNo;
            this.deferredResult = deferredResult;
            this.servletRequest = servletRequest;
            this.servletResponse = servletResponse;
            this.request = request;
            this.method = method;
            this.serviceExecutor = serviceExecutor;
            this.locale = getRequestLocale(servletRequest);
            this.requestUri = getRequestUri(servletRequest);
            this.locale = getRequestLocale(servletRequest);

        }

        private boolean enableLog() {
            return !getClass().isAnnotationPresent(NoControllerLog.class) && !method.isAnnotationPresent(NoControllerLog.class);
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            boolean enableLog = enableLog();
            ScorpioContextUtils.setBizSeqNo(this.bizSeqNo);
            ScorpioContextUtils.setAuthentication(SecurityContextHolder.getContext().getAuthentication());

            try {
                T message = serviceExecutor.execute(request);
                deferredResult.setResult(message);
                if (enableLog) {
                    log.info("{} - request {} SUCCESS, request: {}, response: {}",
                            ScorpioContextUtils.getBizSeqNo(), requestUri, request, message);
                }
            } catch (ScorpioException e) {
                T message = (T) new ScorpioBaseMessage();
                message.setStatus(e.getStatus());
                deferredResult.setResult(message);
                log.info("{} - request {} FAILED, request: {}, response: {}",
                        ScorpioContextUtils.getBizSeqNo(), requestUri, request, message, e);

            } catch (Exception e) {
                T errorMessage = (T) new ScorpioBaseMessage(ScorpioStatus.INTERNAL_ERROR);
                errorMessage.setDebugMsg(e.getMessage());
                deferredResult.setResult(errorMessage);
                log.info("{} - request {} FAILED, request: {}",
                        ScorpioContextUtils.getBizSeqNo(), requestUri, request, e);
            } finally {
                ScorpioContextUtils.unsetContext();
            }
        }
    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            E request,
            ScorpioServiceExecutor<E, T> executor,
            BindingResult bindingResult,
            long timeout
    ) {
        T timeoutMessage = (T) new ScorpioBaseMessage(ScorpioStatus.SYSTEM_TIMEOUT);
        DeferredResult<T> deferredResult = new DeferredResult<>(timeout, timeoutMessage);
        try {
            ServletRequestAttributes attributes = ((ServletRequestAttributes)
                    Objects.requireNonNull(RequestContextHolder.getRequestAttributes()));
            HttpServletRequest servletRequest = attributes.getRequest();
            HttpServletResponse servletResponse = attributes.getResponse();
            Method calledMethod = this.getRequestMethod(servletRequest);
            String bizSeqNo = ScorpioContextUtils.getBizSeqNo() == null ? SeqUtils.nextValue() : ScorpioContextUtils.getBizSeqNo();
            if (bindingResult != null && bindingResult.hasErrors()) {
                T validationMessage = (T) new ScorpioBaseMessage(ScorpioStatus.INVALID_REQUEST);
                validationMessage.setDebugMsg(Objects.requireNonNull(bindingResult.getFieldError()).getField() + bindingResult.getFieldError().getDefaultMessage());
                deferredResult.setResult(validationMessage);
                return deferredResult;
            }
            DeferredResultRunnable<E, T> runnable = new DeferredResultRunnable<>(
                    bizSeqNo, deferredResult, servletRequest, servletResponse, calledMethod, request, executor);
//            getFrontTaskExecutor().submit(runnable);
            DelegatingSecurityContextAsyncTaskExecutor securityContextAsyncTaskExecutor =
                    new DelegatingSecurityContextAsyncTaskExecutor(getFrontTaskExecutor(), SecurityContextHolder.getContext());
            securityContextAsyncTaskExecutor.submit(runnable);
        } catch (Exception e) {
            T errorMessage = (T) new ScorpioBaseMessage(ScorpioStatus.INTERNAL_ERROR);
            errorMessage.setDebugMsg(e.getMessage());
            deferredResult.setResult(errorMessage);
            log.error("scorpio error", e);
        } finally {
            ScorpioContextUtils.unsetContext();
        }

        return deferredResult;

    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            E request, ScorpioServiceExecutor<E, T> executor, BindingResult bindingResult) {
        return execute(request, executor, bindingResult, defaultTimeout);
    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            E request, ScorpioServiceExecutor<E, T> executor) {
        return execute(request, executor, null);
    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(ScorpioServiceExecutor<E, T> executor) {
        return execute(null, executor);
    }

    protected <T> ScorpioRestMessage<T> toRestMessage(T t) {
        return ScorpioRestMessage.from(t);
    }

    protected <T> ScorpioRestMessage<T> toRestMessage(Page<T> tPage) {
        return ScorpioRestMessage.from(tPage);
    }

    protected <T> ScorpioRestMessage<T> toRestMessage(List<T> tList) {
        return ScorpioRestMessage.from(tList);
    }

    protected <T> ScorpioRestMessage<T> toRestMessage(Set<T> tSet) {
        return ScorpioRestMessage.from(tSet);
    }

}
