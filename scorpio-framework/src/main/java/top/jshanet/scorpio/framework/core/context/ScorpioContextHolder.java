package top.jshanet.scorpio.framework.core.context;


/**
 * @author jshanet
 * @since 2020-04-20
 */
public class ScorpioContextHolder {

    private static final ThreadLocal<ScorpioContext> SCORPIO_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static void setContext(ScorpioContext context) {
        SCORPIO_CONTEXT_THREAD_LOCAL.set(context);

    }

    public static ScorpioContext getContext() {
        return SCORPIO_CONTEXT_THREAD_LOCAL.get();
    }

    public static void removeContext() {
        SCORPIO_CONTEXT_THREAD_LOCAL.remove();
    }


}
