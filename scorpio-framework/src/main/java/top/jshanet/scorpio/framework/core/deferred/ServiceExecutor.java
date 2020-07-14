package top.jshanet.scorpio.framework.core.deferred;

/**
 * @author seanjiang
 * @since 2020-07-14
 */
@FunctionalInterface
public interface ServiceExecutor<T> {
    T execute();
}
