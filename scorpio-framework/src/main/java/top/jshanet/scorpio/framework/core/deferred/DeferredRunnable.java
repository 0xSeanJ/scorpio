package top.jshanet.scorpio.framework.core.deferred;

import lombok.SneakyThrows;
import org.springframework.web.context.request.async.DeferredResult;
import top.jshanet.scorpio.framework.util.ScorpioContextUtils;

/**
 * @author seanjiang
 * @since 2020-07-13
 */
public class DeferredRunnable<T> implements Runnable {

    private DeferredResult<T> deferredResult;
    private ServiceExecutor<T> serviceExecutor;
    private String requestNo;

    public DeferredRunnable(String requestNo, DeferredResult<T> deferredResult, ServiceExecutor<T> serviceExecutor) {
        this.deferredResult = deferredResult;
        this.serviceExecutor = serviceExecutor;
        this.requestNo = requestNo;
    }


    @SneakyThrows
    @Override
    public void run() {
        ScorpioContextUtils.setRequestNo(requestNo);
        T result = serviceExecutor.execute();
        deferredResult.setResult(result);
    }
}
