package com.vnpay.core.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vnpay.core.web.contexts.ThreadContextAttributesKeeper;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linhtn on 1/13/2022.
 */
public class CoreUtils {

    public static final String TRACING_ID = "tracing_id";

    public static void insertTracingIdToMDC(String tracingId) {
        MDC.put(TRACING_ID, tracingId);
    }

    public static HttpHeaders httpHeaders() {
        var headers = new HttpHeaders();
        headers.set(TRACING_ID, ThreadContextAttributesKeeper.getRequestAttributes().getTracingId());
        return headers;
    }

    public static Gson gsonWithCamelCase() {
        return gsonBuilder(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private static GsonBuilder gsonBuilder(FieldNamingPolicy type) {
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(type);
        return gsonBuilder;
    }

    public static Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        var resultMap = new HashMap<String, String>();
        var parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            var key = parameterNames.nextElement();
            var value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    public static Map<String, String> buildHeadersMap(HttpServletRequest request) {
        var map = new HashMap<String, String>();

        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    public static Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        var headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            var inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {}
            }

        }

        return stringBuilder.toString();
    }

}
