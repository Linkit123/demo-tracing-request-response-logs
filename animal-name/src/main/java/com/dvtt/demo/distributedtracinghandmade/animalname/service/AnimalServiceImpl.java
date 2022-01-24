package com.dvtt.demo.distributedtracinghandmade.animalname.service;

import com.dvtt.demo.distributedtracinghandmade.animalname.entity.Animal;
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
public class AnimalServiceImpl implements AnimalService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String API_URL = "https://61d2c38bb4c10c001712b58a.mockapi.io/api/v1/animals";

    @Override
    public String getName() {
        var random = new Random();
        var s = HttpUtils.httpGet(API_URL, restTemplate);
        try {
            var animals = objectMapper.readValue(s, new TypeReference<List<Animal>>() {
            });
            var animalNames = animals.stream().map(Animal::getName).collect(Collectors.toList());
            return animalNames.get(random.nextInt(animalNames.size()));
        } catch (Exception ex) {
            return "";
        }

    }

    @Override
    public Animal create(Animal animal) {
        String result = HttpUtils.httpPost(API_URL, animal, restTemplate);
        try {
            return objectMapper.readValue(result, Animal.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
