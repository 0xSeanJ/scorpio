package top.jshanet.scorpio.framework.core.advice;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.exception.ScorpioException;
import top.jshanet.scorpio.framework.status.ScorpioStatus;

/**
 * @author seanjiang
 * @since 2020-07-18
 */
@RestControllerAdvice
public class ScorpioControllerAdvice {

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class})
    public ScorpioResponse badArgumentHandler(Throwable e) {
        return ScorpioResponse.from(ScorpioStatus.DefaultStatus.BAD_ARGUMENT, e.getLocalizedMessage());
    }

    @ExceptionHandler(ScorpioException.class)
    public ScorpioResponse scorpioExceptionHandler(ScorpioException e) {
        return ScorpioResponse.from(e);
    }
}
