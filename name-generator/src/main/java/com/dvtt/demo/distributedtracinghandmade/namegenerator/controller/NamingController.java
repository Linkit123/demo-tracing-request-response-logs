package com.dvtt.demo.distributedtracinghandmade.namegenerator.controller;

import com.dvtt.demo.distributedtracinghandmade.namegenerator.service.NamingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by linhtn on 1/2/2022.
 */
@RequestMapping("/api/v1/names")
@RestController
@AllArgsConstructor
public class NamingController {

    private final NamingService service;

    @GetMapping("random")
    public String naming() {
        return service.naming();
    }

}
