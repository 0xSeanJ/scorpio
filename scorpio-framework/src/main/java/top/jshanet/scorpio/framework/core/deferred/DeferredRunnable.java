package top.jshanet.scorpio.framework.core.deferred;

import lombok.SneakyThrows;
import org.springframework.web.context.request.async.DeferredResult;
import top.jshanet.scorpio.framework.dto.ScorpioRestMessage;
import top.jshanet.scorpio.framework.util.ScorpioContextUtils;

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
    private DeferredExceptionResolver exceptionResolver;

    public DeferredRunnable(String requestNo,
                            DeferredResult<?> deferredResult,
                            ServiceExecutor<R> serviceExecutor,
                            boolean enableRestMessage,
                            DeferredExceptionResolver exceptionResolver) {
        this.mDeferredResult = (DeferredResult<M>) deferredResult;
        this.rDeferredResult = (DeferredResult<R>) deferredResult;
        this.serviceExecutor = serviceExecutor;
        this.requestNo = requestNo;
        this.enableRestMessage = enableRestMessage;
        this.exceptionResolver = exceptionResolver;
    }


    @SneakyThrows
    @Override
    public void run() {
        ScorpioContextUtils.setRequestNo(requestNo);
        try {
            R result = serviceExecutor.execute();
            if (enableRestMessage) {
                mDeferredResult.setResult((M) ScorpioRestMessage.from(result));
            } else {
                rDeferredResult.setResult(result);
            }
        } catch (Throwable throwable) {
            exceptionResolver.resolveException(mDeferredResult, throwable);
        } finally {
            ScorpioContextUtils.unsetContext();
        }

    }
}
