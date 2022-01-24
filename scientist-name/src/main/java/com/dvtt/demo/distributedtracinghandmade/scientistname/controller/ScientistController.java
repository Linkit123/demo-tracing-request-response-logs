package com.dvtt.demo.distributedtracinghandmade.scientistname.controller;

import com.dvtt.demo.distributedtracinghandmade.scientistname.entity.Scientist;
import com.dvtt.demo.distributedtracinghandmade.scientistname.service.ScientistService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Created by linhtn on 1/2/2022.
 */
@RequestMapping("/api/v1/scientists")
@RestController
@AllArgsConstructor
public class ScientistController {
    private final ScientistService service;

    @GetMapping("/random")
    public String getName() {
        return service.getName();
    }

    @PostMapping()
    public Scientist create(@RequestBody Scientist scientist) {
        return service.create(scientist);
    }
}
