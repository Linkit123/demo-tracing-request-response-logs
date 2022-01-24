package com.dvtt.demo.distributedtracinghandmade.namegenerator.controller;

import com.dvtt.demo.distributedtracinghandmade.namegenerator.entity.Animal;
import com.dvtt.demo.distributedtracinghandmade.namegenerator.entity.Scientist;
import com.dvtt.demo.distributedtracinghandmade.namegenerator.service.PartnerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Created by linhtn on 1/3/2022.
 */
@RequestMapping("/api/v1")
@RestController
@AllArgsConstructor
public class PartnerController {

    private final PartnerService service;

    @PostMapping("/scientists")
    public Scientist createScientist(@RequestBody Scientist scientist) {
        return service.createScientist(scientist);
    }

    @PostMapping("/animals")
    public Animal createAnimal(@RequestBody Animal animal) {
        return service.createAnimal(animal);
    }
}
