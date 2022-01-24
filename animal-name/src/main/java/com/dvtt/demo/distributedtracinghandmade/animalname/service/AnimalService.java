package com.dvtt.demo.distributedtracinghandmade.animalname.service;

import com.dvtt.demo.distributedtracinghandmade.animalname.entity.Animal;

/**
 * Created by linhtn on 1/2/2022.
 */
public interface AnimalService {
    String getName();
    Animal create(Animal animal);
}
