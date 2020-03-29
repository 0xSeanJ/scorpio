package top.jshanet.scorpio.framework.common.service;

import top.jshanet.scorpio.framework.common.exception.ScorpioException;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@FunctionalInterface
public interface ScorpioServiceExecutor<E, T> {

    T execute(E request) throws ScorpioException;
}
