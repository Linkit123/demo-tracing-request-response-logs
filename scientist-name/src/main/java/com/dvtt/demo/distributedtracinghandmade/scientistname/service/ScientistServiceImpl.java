package com.dvtt.demo.distributedtracinghandmade.scientistname.service;

import com.dvtt.demo.distributedtracinghandmade.scientistname.entity.Scientist;
import com.dvtt.demo.utils.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by linhtn on 1/2/2022.
 */
@Service
@AllArgsConstructor
public class ScientistServiceImpl implements ScientistService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String API_URL = "https://61d2c38bb4c10c001712b58a.mockapi.io/api/v1/scientists";

    @Override
    public String getName() {
        var random = new Random();
        var s = HttpUtils.httpGet(API_URL, restTemplate);
        try {
            var scientists = objectMapper.readValue(s, new TypeReference<List<Scientist>>() {
            });
            var stringNames = scientists.stream().map(Scientist::getName).collect(Collectors.toList());
            return stringNames.get(random.nextInt(scientists.size()));
        } catch (Exception ex) {
            return "";
        }

    }

    @Override
    public Scientist create(Scientist animal) {
        String result = HttpUtils.httpPost(API_URL, animal, restTemplate);
        try {
            return objectMapper.readValue(result, Scientist.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


}
