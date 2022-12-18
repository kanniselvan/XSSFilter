package com.xssFilter.exception;

import javax.servlet.ServletException;

public class XSSServletException extends RuntimeException {

    public XSSServletException() {
    }

    public XSSServletException(String message) {
        super(message);
    }
}
