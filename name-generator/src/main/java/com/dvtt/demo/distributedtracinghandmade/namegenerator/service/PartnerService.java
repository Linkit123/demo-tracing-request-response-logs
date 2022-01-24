package com.dvtt.demo.distributedtracinghandmade.namegenerator.service;

import com.dvtt.demo.distributedtracinghandmade.namegenerator.entity.Animal;
import com.dvtt.demo.distributedtracinghandmade.namegenerator.entity.Scientist;

/**
 * Created by linhtn on 1/3/2022.
 */
public interface PartnerService {
    Scientist createScientist(Scientist scientist);
    Animal createAnimal(Animal animal);
}
