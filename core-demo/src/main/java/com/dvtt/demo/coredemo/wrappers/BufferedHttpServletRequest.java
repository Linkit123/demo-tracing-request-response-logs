package com.dvtt.demo.coredemo.wrappers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by linhtn on 1/21/2022.
 */
@Slf4j
public class BufferedHttpServletRequest extends HttpServletRequestWrapper {

    public final String payload;

    public BufferedHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        String contentBody = IOUtils.toString(request.getReader());
        if (!ObjectUtils.isEmpty(request.getContentType()) && request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            var gson = new Gson();
            var jsonElement = gson.fromJson(contentBody, JsonElement.class);
            payload = gson.toJson(jsonElement);
        } else {
            payload = contentBody;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

}
