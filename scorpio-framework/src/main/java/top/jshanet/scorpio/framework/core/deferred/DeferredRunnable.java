package top.jshanet.scorpio.framework.core.deferred;

import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.async.DeferredResult;
import top.jshanet.scorpio.framework.dto.ScorpioRestMessage;
import top.jshanet.scorpio.framework.util.ScorpioContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author seanjiang
 * @since 2020-07-13
 */
@SuppressWarnings("unchecked")
public class DeferredRunnable<R, M> implements Runnable {

    private DeferredResult<R> rDeferredResult;
    private DeferredResult<M> mDeferredResult;
    private ServiceExecutor<R> serviceExecutor;
    private String requestNo;
    private boolean enableRestMessage;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private DeferredExceptionResolver exceptionResolver;

    public DeferredRunnable(String requestNo,
                            DeferredResult<?> deferredResult,
                            ServiceExecutor<R> serviceExecutor,
                            boolean enableRestMessage,
                            HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse,
                            DeferredExceptionResolver exceptionResolver) {
        this.mDeferredResult = (DeferredResult<M>) deferredResult;
        this.rDeferredResult = (DeferredResult<R>) deferredResult;
        this.serviceExecutor = serviceExecutor;
        this.requestNo = requestNo;
        this.enableRestMessage = enableRestMessage;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.exceptionResolver = exceptionResolver;
    }


    @SneakyThrows
    @Override
    public void run() {
        ScorpioContextUtils.setRequestNo(requestNo);
        ScorpioContextUtils.setAuthentication(SecurityContextHolder.getContext().getAuthentication());
        ScorpioContextUtils.setHttpServletRequest(httpServletRequest);
        ScorpioContextUtils.setHttpServletResponse(httpServletResponse);
        try {
            R result = serviceExecutor.execute();
            if (enableRestMessage) {
                mDeferredResult.setResult((M) ScorpioRestMessage.from(result));
            } else {
                rDeferredResult.setResult(result);
            }
        } catch (Throwable throwable) {
            exceptionResolver.resolveException(mDeferredResult, throwable);
        }
        ScorpioContextUtils.unsetContext();
    }
}
