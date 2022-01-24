package com.dvtt.demo.coredemo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by linhtn on 1/3/2022.
 */
public interface LoggingService {

    void logRequest(HttpServletRequest httpServletRequest, Object body);

    void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body);

}
