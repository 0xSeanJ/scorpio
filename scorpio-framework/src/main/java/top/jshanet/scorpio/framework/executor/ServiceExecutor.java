package top.jshanet.scorpio.framework.executor;

import top.jshanet.scorpio.framework.exception.ScorpioException;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@FunctionalInterface
public interface ServiceExecutor<E, T> {

    T execute(E request) throws ScorpioException;
}
