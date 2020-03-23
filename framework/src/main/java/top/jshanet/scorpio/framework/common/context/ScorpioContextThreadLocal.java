package top.jshanet.scorpio.framework.common.context;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
public class ScorpioContextThreadLocal {


    public static final ThreadLocal<ScorpioContext> USER_CONTEXT_THREAD_LOCAL =
            new ThreadLocal<>();

    public static void set(ScorpioContext userContext) {
        USER_CONTEXT_THREAD_LOCAL.set(userContext);
    }

    public static void unset() {
        USER_CONTEXT_THREAD_LOCAL.remove();
    }

    public static ScorpioContext get() {
        return USER_CONTEXT_THREAD_LOCAL.get();
    }

}
