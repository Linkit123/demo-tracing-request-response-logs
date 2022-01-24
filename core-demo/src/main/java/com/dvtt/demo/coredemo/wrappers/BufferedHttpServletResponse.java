package com.dvtt.demo.coredemo.wrappers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * Created by linhtn on 1/21/2022.
 */
public class BufferedHttpServletResponse extends HttpServletResponseWrapper {

    TeeServletOutputStream teeStream;

    PrintWriter teeWriter;

    ByteArrayOutputStream bos;
    HttpServletResponse response;

    public BufferedHttpServletResponse(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    public String getContent() throws IOException {
        if (!ObjectUtils.isEmpty(response.getContentType()) && response.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            var gson = new Gson();
            var jsonElement = gson.fromJson(bos.toString(), JsonElement.class);
            return gson.toJson(jsonElement);
        }
        if (ObjectUtils.isEmpty(bos)) {
            return "";
        }
        return bos.toString();
    }

    @Override
    public PrintWriter getWriter() throws IOException {

        if (this.teeWriter == null) {
            this.teeWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
        }
        return this.teeWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {

        if (teeStream == null) {
            bos = new ByteArrayOutputStream();
            teeStream = new TeeServletOutputStream(getResponse().getOutputStream(), bos);
        }
        return teeStream;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (teeStream != null) {
            teeStream.flush();
            System.err.println("teeStream flush");
        }
        if (this.teeWriter != null) {
            this.teeWriter.flush();
            System.err.println("teeWriter flush");
        }
    }

    public static class TeeServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public TeeServletOutputStream(OutputStream one, OutputStream two) {
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

    }

}
