package com.dvtt.demo.distributedtracinghandmade.namegenerator.service.impl;

import com.dvtt.demo.distributedtracinghandmade.namegenerator.service.NamingService;
import com.dvtt.demo.utils.HttpUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by linhtn on 1/2/2022.
 */
@Service
@AllArgsConstructor
public class NamingServiceImpl implements NamingService {

    private final RestTemplate restTemplate;
    private static final String SCIENTIST_URL = "http://localhost:8069/api/v1/scientists/random";
    private static final String ANIMAL_URL = "http://localhost:8096/api/v1/animals/random";

    public String naming() {
        String scientistName = HttpUtils.httpGet(SCIENTIST_URL, restTemplate);
        String animalName = HttpUtils.httpGet(ANIMAL_URL, restTemplate);
        return String.format("%s %s %s", scientistName, "loves", animalName);
    }

}
