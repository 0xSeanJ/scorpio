package io.github.jshanet.scorpio.framework.service;

import io.github.jshanet.scorpio.common.exception.ScorpioException;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@FunctionalInterface
public interface ScorpioServiceExecutor<E, T> {
    T execute(E request) throws ScorpioException;

}
