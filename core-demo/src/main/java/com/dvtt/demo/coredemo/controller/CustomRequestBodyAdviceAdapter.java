package com.dvtt.demo.coredemo.controller;

import com.dvtt.demo.coredemo.service.LoggingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by linhtn on 1/3/2022.
 */
@ControllerAdvice
@AllArgsConstructor
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    final LoggingServiceImpl loggingService;

    final HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    // for post method
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
                                MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        loggingService.logRequest(httpServletRequest, body);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
