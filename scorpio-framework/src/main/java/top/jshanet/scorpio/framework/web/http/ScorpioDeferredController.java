package top.jshanet.scorpio.framework.web.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.DeferredResult;
import top.jshanet.scorpio.framework.autoconfig.ScorpioProperties;
import top.jshanet.scorpio.framework.core.deferred.DeferredRunnable;
import top.jshanet.scorpio.framework.core.deferred.ServiceExecutor;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.service.RequestNoService;
import top.jshanet.scorpio.framework.status.ScorpioStatus;

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
    private ScorpioProperties scorpioProperties;

    @Autowired
    private RequestNoService requestNoService;

    protected DeferredResult<?> execute(ServiceExecutor<?> serviceExecutor) {
        String requestNo = requestNoService.nextRequestNo();
        ScorpioResponse timeoutResponse = new ScorpioResponse(ScorpioStatus.DefaultStatus.WEB_TIMEOUT);
        DeferredResult<?> deferredResult = new DeferredResult<>(scorpioProperties.getWeb().getTimeout(), timeoutResponse);
        DeferredRunnable<?> deferredRunnable = new DeferredRunnable(requestNo, deferredResult, serviceExecutor);
        threadPoolTaskExecutor.submit(deferredRunnable);
        return deferredResult;
    }


}
