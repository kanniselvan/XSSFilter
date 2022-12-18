package com.xssFilter.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xssFilter.exception.ExceptionController;
import com.xssFilter.exception.XSSServletException;
import com.xssFilter.model.ErrorResponse;
import com.xssFilter.utils.XSSValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

@Component
public class ResponseFilter implements Filter {

    ObjectMapper objectMapper = new ObjectMapper();


    @Value("#{'${skip_words}'.split(',')}")
    private List<String> skipWords;

    @Autowired
    private ExceptionController exceptionController;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest, skipWords);

            String uri = requestWrapper.getRequestURI();
            System.out.println("getRequestURI : " + uri);
            String decodedURI = URLDecoder.decode(uri, "UTF-8");
            System.out.println("decodedURI : " + decodedURI);

            // XSS:  Path Variable Validation
            if (!XSSValidationUtils.isValidURL(decodedURI, skipWords)) {
                ErrorResponse errorResponse = new ErrorResponse();

                errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
                errorResponse.setMessage("XSS attack error");
                System.out.println("convertObjectToJson(errorResponse) : " + convertObjectToJson(errorResponse));
                servletResponse.getWriter().write(convertObjectToJson(errorResponse));
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }

            System.out.println("Response output: " + requestWrapper.getBody());
            if (!StringUtils.isEmpty(requestWrapper.getBody())) {

                // XSS:  Post Body data validation
                if (XSSValidationUtils.isValidURLPattern(requestWrapper.getBody(), skipWords)) {

                    filterChain.doFilter(requestWrapper, servletResponse);
                } else {
                    ErrorResponse errorResponse = new ErrorResponse();

                    errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
                    errorResponse.setMessage("XSS attack error");
                    servletResponse.getWriter().write(convertObjectToJson(errorResponse));
                    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                    return;

                }
            } else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        } catch (XSSServletException ex) {
            servletResponse.getWriter().write(ex.getMessage());
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        }  catch (Exception ex) {
            servletResponse.getWriter().write(ex.getMessage());
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            System.out.println("clean up");
        }
    }


    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}


