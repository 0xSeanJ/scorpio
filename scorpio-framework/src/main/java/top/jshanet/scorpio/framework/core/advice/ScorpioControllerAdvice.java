package top.jshanet.scorpio.framework.core.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.dto.ScorpioRestMessage;
import top.jshanet.scorpio.framework.exception.*;
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
    public ScorpioResponse badRequestHandler(Exception e) {
        return ScorpioResponse.fromException(new BadRequestException(e.getLocalizedMessage()));
    }

    @ExceptionHandler(ScorpioException.class)
    public ScorpioResponse scorpioExceptionHandler(ScorpioException e) {
        if (e.getData() != null) {
            return ScorpioRestMessage.fromStatus(e.getStatus(), e.getData());
        }
        return ScorpioResponse.fromException(e);
    }

    @ExceptionHandler(Throwable.class)
    public ScorpioResponse unknownHandler(Throwable throwable) {
        log.error("{} - unknown error", ScorpioContextUtils.getRequestNo(), throwable);
        return ScorpioResponse.fromStatus(ScorpioStatus.DefaultStatus.UNKNOWN_ERROR, throwable.getLocalizedMessage());
    }
}
