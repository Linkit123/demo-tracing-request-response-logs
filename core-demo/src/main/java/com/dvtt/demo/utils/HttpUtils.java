package com.dvtt.demo.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by linhtn on 1/3/2022.
 */
@Component
public class HttpUtils {

    public static String httpPost(String url, Object body, RestTemplate restTemplate) {
        var uri = UriComponentsBuilder.fromHttpUrl(url)
                .build().toString();
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, "vi-VN");

        var exchange = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
        return exchange.getBody();
    }

    public static String httpGet(String url, RestTemplate restTemplate) {
        var uri = UriComponentsBuilder.fromHttpUrl(url)
                .build().toString();
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, "vi-VN");
        var exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return exchange.getBody();
    }

}
