package com.dvtt.demo.coredemo.filter;

import com.dvtt.demo.coredemo.thread.ThreadContextKeeper;
import com.dvtt.demo.coredemo.wrappers.BufferedRequestWrapper;
import com.dvtt.demo.coredemo.wrappers.BufferedResponseWrapper;
import com.dvtt.demo.utils.CoreUtils;
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
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.dvtt.demo.utils.CoreUtils.TRACING_ID;

/**
 * Created by linhtn on 1/21/2022.
 */
@Component
@Slf4j
public class CustomFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {
        var tracingId = UUID.randomUUID().toString();
        MDC.put(TRACING_ID, tracingId);
        try {

            Map<String, String> requestParam = CoreUtils.getRequestParam(httpServletRequest);
            BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(
                    httpServletRequest);
            BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(
                    httpServletResponse);

            if (!ObjectUtils.isEmpty(bufferedRequest.getHeader(TRACING_ID))) {
                tracingId = bufferedRequest.getHeader(TRACING_ID);
            }
            var requestAttributes = ThreadContextKeeper.getRequestAttributes();
            requestAttributes.setTracingId(tracingId);
            requestAttributes.setAcceptLanguage("vi-VN");
            if (!StringUtils.isEmpty(bufferedRequest.getHeader("Accept-Language"))) {
                requestAttributes.setAcceptLanguage(bufferedRequest.getHeader("Accept-Language"));
            }
            var requestBody = bufferedRequest.getRequestBody();
            var logMessage = new StringBuilder().append("[REQ_LOG] ====> ")
                    .append("[STATUS CODE: ").append(bufferedResponse.getStatus()).append("]")
                    .append(", [URI: ").append(CoreUtils.getFullURL(bufferedRequest)).append("]")
                    .append(", [REQUEST HEADERS: ").append(CoreUtils.buildHeadersMap(bufferedRequest)).append("]")
                    .append(", [REQUEST CONTENT TYPE: ").append(bufferedRequest.getContentType()).append("]")
                    .append(", [REMOTE ADDRESS: ").append(bufferedRequest.getRemoteAddr()).append("]")
                    .append(", [METHOD: ").append(bufferedRequest.getMethod()).append("]");
            if (!ObjectUtils.isEmpty(requestBody)) {
                logMessage.append(", [REQUEST BODY: ").append(requestBody).append("]");
            }

            filterChain.doFilter(bufferedRequest, bufferedResponse);

            var responseBody = bufferedResponse.getContent();
            if (!ObjectUtils.isEmpty(requestParam)) {
                logMessage.append(", [REQUEST PARAMS: ").append(requestParam).append("]");
            }

            if (!ObjectUtils.isEmpty(responseBody)) {
                logMessage.append(", [RESPONSE BODY: ").append(responseBody).append("]");
            }

            log.info(logMessage.toString());
        } catch (Throwable a) {
            log.error(a.getMessage());
        } finally {
            MDC.remove(TRACING_ID);
        }
    }

}
