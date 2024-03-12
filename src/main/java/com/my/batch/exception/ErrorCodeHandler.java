package com.my.batch.exception;

import com.my.batch.constant.ResultCode;
import com.my.batch.exception.error.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorCodeHandler {

    @ExceptionHandler(UndefinedRequestApiException.class)
    private ResponseEntity<Object> handleUndefinedRequestApiExceptionException(final ParsingException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(ExceededMaximumRequestsException.class)
    private ResponseEntity<Object> handleExceededMaximumRequestsException(final ParsingException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(ParsingException.class)
    private ResponseEntity<Object> handleParsingException(final ParsingException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(InvalidValueException.class)
    private ResponseEntity<Object> handleInvalidValueException(final InvalidValueException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(NotFoundUserException.class)
    private ResponseEntity<Object> handleNotMatchedException(final NotFoundUserException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    private ResponseEntity<Object> handleUnauthorizedException(final UnauthorizedException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(UncaughtException.class)
    private ResponseEntity<Object> handleUncaughtException(final UncaughtException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(CryptoErrorException.class)
    private ResponseEntity<Object> handleCryptoErrorException(final CryptoErrorException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<Object> handleInvalidTokenException(final InvalidTokenException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedScrapException.class)
    private ResponseEntity<Object> handleUnauthorizedScrapException(final UnauthorizedScrapException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(NotAllowedUsertException.class)
    private ResponseEntity<Object> handleNotAllowedUserException(final NotAllowedUsertException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(DuplicatedException.class)
    private ResponseEntity<Object> handleDuplicatedException(final DuplicatedException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(e.getResultCode(), httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, e.getResultCode().getHttpStatus());
    }

    @ExceptionHandler(BindException.class)
    private ResponseEntity<Object> handleBindException(final BindException e, HttpServletRequest httpServletRequest) {
        ResultCode resultCode = ResultCode.INVALID;
        ErrorResponse response = ErrorResponse.of(resultCode, httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, resultCode.getHttpStatus());
    }

    @ExceptionHandler(ServletException.class)
    private ResponseEntity<Object> handleServletException(final ServletException e, HttpServletRequest httpServletRequest) {
        ResultCode resultCode = ResultCode.INVALID;
        ErrorResponse response = ErrorResponse.of(resultCode, httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, resultCode.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleException(final Exception e, HttpServletRequest httpServletRequest) {
        ResultCode resultCode = ResultCode.INVALID;
        ErrorResponse response = ErrorResponse.of(resultCode, httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, resultCode.getHttpStatus());
    }

}
