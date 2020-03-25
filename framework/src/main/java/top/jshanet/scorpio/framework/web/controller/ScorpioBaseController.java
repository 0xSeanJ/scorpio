package top.jshanet.scorpio.framework.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindingResult;
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
import top.jshanet.scorpio.framework.common.util.ScorpioContextUtil;
import top.jshanet.scorpio.framework.common.util.SeqUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

        private boolean isAnnotationOnClassOrMethod(Class<? extends Annotation> c) {
            return getClass().isAnnotationPresent(c) && method.isAnnotationPresent(c);
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            ScorpioContextUtil.setBizSeqNo(this.bizSeqNo);
            try {
                T message = serviceExecutor.execute(request);
                deferredResult.setResult(message);
                if (!isAnnotationOnClassOrMethod(NoControllerLog.class)) {
                    log.info("{} SUCCESS:\nlocale: {}\nurl: {}\nrequest: {}\nresponse: {}\n",
                            method, locale, requestUri, request, message);
                }
            } catch (ScorpioException e) {
                T message = (T) new ScorpioBaseMessage();
                message.setStatus(e.getStatus());
                deferredResult.setResult(message);
                if (!isAnnotationOnClassOrMethod(NoControllerLog.class)) {
                    log.warn("{} FAIL:\nlocale: {}\nurl: {}\nrequest: {}\nresponse: {}\n",
                            method, locale, requestUri, request, message, e);
                }
            } catch (Exception e) {
                T errorMessage = (T) new ScorpioBaseMessage(ScorpioStatus.INTERNAL_ERROR);
                errorMessage.setDebugMsg(e.getMessage());
                deferredResult.setResult(errorMessage);
                if (!isAnnotationOnClassOrMethod(NoControllerLog.class)) {
                    log.error("{} ERROR:\nlocale: {}\nurl: {}\nrequest: {}\n",
                            method, locale, requestUri, request, e);
                }
            } finally {
                ScorpioContextUtil.unsetContext();
            }
        }
    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            E request, ScorpioServiceExecutor<E, T> executor, BindingResult bindingResult,
            long timeout) {
        T timeoutMessage = (T) new ScorpioBaseMessage(ScorpioStatus.SYSTEM_TIMEOUT);
        DeferredResult<T> deferredResult = new DeferredResult<>(timeout, timeoutMessage);
        try {
            Method calledMethod = this.getRequestMethod(servletRequest);
            String bizSeqNo = ScorpioContextUtil.getBizSeqNo() == null ? SeqUtil.nextValue() : ScorpioContextUtil.getBizSeqNo();
            if (bindingResult != null && bindingResult.hasErrors()) {
                T validationMessage = (T) new ScorpioBaseMessage(ScorpioStatus.INVALID_REQUEST);
                validationMessage.setDebugMsg(bindingResult.getFieldError().getField() + bindingResult.getFieldError().getDefaultMessage());
                deferredResult.setResult(validationMessage);
                return deferredResult;
            }
            DeferredResultRunnable<E, T> runnable = new DeferredResultRunnable<>(
                    bizSeqNo, deferredResult, servletRequest, servletResponse, calledMethod, request, executor);
            getFrontTaskExecutor().submit(runnable);
//            DelegatingSecurityContextAsyncTaskExecutor securityContextAsyncTaskExecutor =
//                    new DelegatingSecurityContextAsyncTaskExecutor(frontTaskExecutor, SecurityContextHolder.getContext());
//            securityContextAsyncTaskExecutor.submit(runnable);
        } catch (Exception e) {
            T errorMessage = (T) new ScorpioBaseMessage(ScorpioStatus.INTERNAL_ERROR);
            errorMessage.setDebugMsg(e.getMessage());
            deferredResult.setResult(errorMessage);
            log.error("controller error", e);
        } finally {
            ScorpioContextUtil.unsetContext();
        }

        return deferredResult;

    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            E request, ScorpioServiceExecutor<E, T> executor, BindingResult bindingResult) {
        return execute(servletRequest, servletResponse, request, executor, bindingResult, defaultTimeout);
    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            E request, ScorpioServiceExecutor<E, T> executor) {
        return execute(servletRequest, servletResponse, request, executor, null);
    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            E request, ScorpioServiceExecutor<E, T> executor, BindingResult bindingResult) {
        return execute(null, null, request, executor, bindingResult);
    }

    protected <E, T extends ScorpioBaseMessage> DeferredResult<T> execute(
            E request, ScorpioServiceExecutor<E, T> executor) {
        return execute(request, executor, null);
    }


    protected <T> ScorpioRestMessage<T> toScorpioRestMessage(T t) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setResult(t);
        return restMessage;
    }

    protected <T> ScorpioRestMessage<T> toScorpioRestMessage(Page<T> tList) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setData(tList.getContent());
        restMessage.setCount(tList.getTotalElements());
        return restMessage;
    }

    protected <T> ScorpioRestMessage<T> toScorpioRestMessage(List<T> tList) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setData(tList);
        restMessage.setCount((long) tList.size());
        return restMessage;
    }

    protected <T> ScorpioRestMessage<T> toScorpioRestMessage(Set<T> tSet) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setData(new ArrayList<>(tSet));
        restMessage.setCount((long) tSet.size());
        return restMessage;
    }

}
