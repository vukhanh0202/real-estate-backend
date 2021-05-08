package com.uit.realestate.exception;

import com.uit.realestate.payload.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Component
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle exception.
     *
     * @param ex      exception
     * @param request request
     * @return ExceptionResponse
     */
    @ExceptionHandler({NotFoundException.class, InvalidException.class, BadRequestException.class,ForbiddenException.class})
    public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
        HttpStatus httpStatus;
        if (ex instanceof NotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (ex instanceof BadRequestException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof InvalidException) {
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
        } else if (ex instanceof ForbiddenException) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), httpStatus.value());
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
}

