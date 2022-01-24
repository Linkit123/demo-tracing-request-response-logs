package com.dvtt.demo.distributedtracinghandmade.animalname.controller;

import com.dvtt.demo.distributedtracinghandmade.animalname.entity.Animal;
import com.dvtt.demo.distributedtracinghandmade.animalname.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Created by linhtn on 1/2/2022.
 */
@RequestMapping("/api/v1/animals")
@RestController
@AllArgsConstructor
public class AnimalController {
    private final AnimalService service;

    @GetMapping("/random")
    public String getName() {
        return service.getName();
    }

    @PostMapping()
    public Animal create(@RequestBody Animal animal) {
        return service.create(animal);
    }
}
