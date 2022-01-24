package com.dvtt.demo.distributedtracinghandmade.namegenerator.service.impl;

import com.dvtt.demo.distributedtracinghandmade.namegenerator.entity.Animal;
import com.dvtt.demo.distributedtracinghandmade.namegenerator.entity.Scientist;
import com.dvtt.demo.distributedtracinghandmade.namegenerator.service.PartnerService;
import com.dvtt.demo.utils.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by linhtn on 1/3/2022.
 */
@Service
@AllArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String SCIENTIST_URL = "http://localhost:8069/api/v1/scientists";
    private static final String ANIMAL_URL = "http://localhost:8096/api/v1/animals";

    @Override
    public Scientist createScientist(Scientist scientist) {
        try {
            String result = HttpUtils.httpPost(SCIENTIST_URL, scientist, restTemplate);
            return objectMapper.readValue(result, Scientist.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public Animal createAnimal(Animal animal) {
        try {
            String result = HttpUtils.httpPost(ANIMAL_URL, animal, restTemplate);
            return objectMapper.readValue(result, Animal.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
