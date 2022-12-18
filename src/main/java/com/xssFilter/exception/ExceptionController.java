package com.xssFilter.exception;

import com.xssFilter.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    //sample handler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(XSSServletException.class)
    public ErrorResponse handleSQLException(HttpServletRequest request,
                                Exception ex){
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
        errorResponse.setMessage(ex.getMessage());

        return errorResponse;
    }
    //other handlers
}