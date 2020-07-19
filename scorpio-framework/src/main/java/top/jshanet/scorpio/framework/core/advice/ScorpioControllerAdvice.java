package top.jshanet.scorpio.framework.core.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.exception.BadArgumentException;
import top.jshanet.scorpio.framework.exception.ScorpioException;
import top.jshanet.scorpio.framework.status.ScorpioStatus;
import top.jshanet.scorpio.framework.util.ScorpioContextUtils;

import javax.persistence.EntityNotFoundException;

/**
 * @author seanjiang
 * @since 2020-07-18
 */
@Slf4j
@RestControllerAdvice
public class ScorpioControllerAdvice {

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class})
    public ScorpioResponse badArgumentHandler(Throwable e) {
        return ScorpioResponse.from(ScorpioStatus.DefaultStatus.BAD_ARGUMENT, e.getLocalizedMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ScorpioResponse entityNotFoundHandler(EntityNotFoundException e) {
        return ScorpioResponse.from(new BadArgumentException(e.getLocalizedMessage()));
    }
    @ExceptionHandler(ScorpioException.class)
    public ScorpioResponse scorpioExceptionHandler(ScorpioException e) {
        return ScorpioResponse.from(e);
    }

    @ExceptionHandler(Throwable.class)
    public ScorpioResponse unknownHandler(Throwable throwable) {
        log.error("{} - unknown error", ScorpioContextUtils.getRequestNo(), throwable);
        return ScorpioResponse.from(ScorpioStatus.DefaultStatus.UNKNOWN_ERROR, throwable.getLocalizedMessage());
    }
}
