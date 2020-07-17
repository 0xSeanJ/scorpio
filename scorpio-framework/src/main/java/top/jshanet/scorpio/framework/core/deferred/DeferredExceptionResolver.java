package top.jshanet.scorpio.framework.core.deferred;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.status.ScorpioStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author seanjiang
 * @since 2020-07-18
 */
public class DeferredExceptionResolver implements InitializingBean {

    private ApplicationContext applicationContext;

    private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache =
            new LinkedHashMap<>();

    public DeferredExceptionResolver(ApplicationContext context) {
        this.applicationContext = context;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void afterPropertiesSet() {
        initExceptionHandlerAdviceCache();
    }

    private void initExceptionHandlerAdviceCache() {

        if (getApplicationContext() == null) {
            return;
        }

        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
            }
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMappings()) {
                this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void resolveException(DeferredResult deferredResult, Throwable throwable) {
        try {
            for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : exceptionHandlerAdviceCache.entrySet()) {
                ControllerAdviceBean adviceBean = entry.getKey();
                ExceptionHandlerMethodResolver resolver = entry.getValue();
                Method method = resolver.resolveMethodByThrowable(throwable);
                if (method != null) {
                    deferredResult.setResult(method.invoke(adviceBean.getBeanType().getDeclaredConstructor().newInstance(), throwable));
                }
            }
        } catch (Exception e) {
            deferredResult.setResult(
                    ScorpioResponse.from(ScorpioStatus.DefaultStatus.UNKNOWN_ERROR,
                            e.getLocalizedMessage()));
        }
    }
}
