package com.dvtt.demo.utils;

import com.dvtt.demo.coredemo.thread.ThreadContextKeeper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.dvtt.demo.coredemo.thread.ThreadContextKeeper.getRequestAttributes;

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
        headers.set(TRACING_ID, getRequestAttributes().getTracingId());
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

    public static Map<String, String> getRequestParam(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<String, String>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue;
            if (requestParamName.equalsIgnoreCase("password")) {
                requestParamValue = "********";
            } else {
                requestParamValue = request.getParameter(requestParamName);
            }
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
    }
}
