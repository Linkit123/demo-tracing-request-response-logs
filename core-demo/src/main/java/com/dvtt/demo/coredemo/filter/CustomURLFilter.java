package com.dvtt.demo.coredemo.filter;

import com.dvtt.demo.coredemo.wrappers.BufferedHttpServletRequest;
import com.dvtt.demo.coredemo.wrappers.BufferedHttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.UUID;

import static com.dvtt.demo.coredemo.thread.ThreadContextKeeper.getRequestAttributes;
import static com.dvtt.demo.utils.CoreUtils.*;

/**
 * Created by linhtn on 1/3/2022.
 */
@Slf4j
@Component
public class CustomURLFilter extends OncePerRequestFilter {

    @Override
    protected boolean isAsyncDispatch(@NotNull final HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        var requestWrapper = new BufferedHttpServletRequest(httpServletRequest);
        var responseWrapper = new BufferedHttpServletResponse(httpServletResponse);

        var tracingId = UUID.randomUUID().toString();
        if (!ObjectUtils.isEmpty(requestWrapper.getHeader(TRACING_ID))) {
            tracingId = requestWrapper.getHeader(TRACING_ID);
        }
        var requestAttributes = getRequestAttributes();
        requestAttributes.setTracingId(tracingId);
        requestAttributes.setAcceptLanguage("vi-VN");
        if (!StringUtils.isEmpty(requestWrapper.getHeader("Accept-Language"))) {
            requestAttributes.setAcceptLanguage(requestWrapper.getHeader("Accept-Language"));
        }

        MDC.put(TRACING_ID, tracingId);

        var requestBody = requestWrapper.payload;

        filterChain.doFilter(requestWrapper, responseWrapper);

        var responseBody = responseWrapper.getContent();
        var stringBuilder = new StringBuilder().append("[REQ_LOG] ")
                .append("StatusCode: ").append(responseWrapper.getStatus())
                .append(", URI: ").append(getFullURL(requestWrapper))
                .append(", Request Headers: ").append(buildHeadersMap(requestWrapper))
                .append(", Request ContentType: ").append(requestWrapper.getContentType())
                .append(", Remote Address: ").append(requestWrapper.getRemoteAddr())
                .append(", Method: ").append(requestWrapper.getMethod());

        if (!ObjectUtils.isEmpty(requestBody)) {
            stringBuilder.append(", Request Body: ").append(requestBody);
        }

        if (!ObjectUtils.isEmpty(responseBody)) {
            stringBuilder.append(", Response Body: ").append(responseBody);
        }

        log.info(stringBuilder.toString());
        MDC.remove(TRACING_ID);
    }

}
