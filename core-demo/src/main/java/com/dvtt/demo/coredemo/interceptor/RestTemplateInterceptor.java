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
        try {
            var data = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            watcher.stop();
            String stringBuilder = "[REQ_REST_LOG] ----> " +
                    "[STATUS CODE: " + response.getStatusCode() + "]" +
                    ", [URI: " + request.getURI() + "]" +
                    ", [METHOD: " + request.getMethod() + "]" +
                    ", [STATUS TEXT: " + response.getStatusText() + "]" +
                    ", [REQUEST HEADERS: " + request.getHeaders() + "]";
            if (!ObjectUtils.isEmpty(stringBuilder)) {
                stringBuilder += ", [REQUEST BODY :" + new String(body, StandardCharsets.UTF_8) + "]";
            }

            if (!ObjectUtils.isEmpty(stringBuilder)) {
                stringBuilder += ", [RESPONSE BODY: " + data + "]";
            }
            stringBuilder += ", [RESPONSE TIME :" + watcher.getTotalTimeMillis() + "]";
            log.info(stringBuilder);
        } catch (Throwable a) {
            log.error(a.getMessage());
        }

    }
}
