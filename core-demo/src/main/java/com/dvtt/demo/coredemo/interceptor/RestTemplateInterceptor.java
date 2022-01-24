package com.dvtt.demo.coredemo.interceptor;

import com.dvtt.demo.coredemo.wrappers.BufferedClientHttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.dvtt.demo.coredemo.thread.ThreadContextKeeper.getRequestAttributes;
import static com.dvtt.demo.utils.CoreUtils.TRACING_ID;

@Slf4j
@AllArgsConstructor
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var watcher = new StopWatch();
        watcher.start();
        var requestAttributes = getRequestAttributes();
        String tracingId = requestAttributes.getTracingId();
        if (ObjectUtils.isEmpty(tracingId)) {
            tracingId = UUID.randomUUID().toString();
            requestAttributes.setTracingId(tracingId);
        }
        MDC.put(TRACING_ID, tracingId);
        var response = execution.execute(request, body);
        response = new BufferedClientHttpResponse(response);
        this.logResponse(request, response, body, watcher);
        return response;
    }

    private void logResponse(HttpRequest request, ClientHttpResponse response, byte[] body, StopWatch watcher) throws IOException {
        var data = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
        watcher.stop();
        String stringBuilder = "[REQ_HTTP_LOG] REQUEST: " +
                "Status code: " + response.getStatusCode() +
                ", URI: " + request.getURI() +
                ", Method: " + request.getMethod() +
                ", Status text: " + response.getStatusText() +
                ", Request Headers: " + request.getHeaders();
        if (!ObjectUtils.isEmpty(stringBuilder)) {
            stringBuilder += ", Request body :" + new String(body, StandardCharsets.UTF_8);
        }

        if (!ObjectUtils.isEmpty(stringBuilder)) {
            stringBuilder += ", Response body: " + data;
        }
        stringBuilder += ", Response time :" + watcher.getTotalTimeMillis();
        log.info(stringBuilder);
    }

}
