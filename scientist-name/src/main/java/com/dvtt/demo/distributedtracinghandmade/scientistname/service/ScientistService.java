package com.dvtt.demo.distributedtracinghandmade.scientistname.service;

import com.dvtt.demo.distributedtracinghandmade.scientistname.entity.Scientist;

/**
 * Created by linhtn on 1/2/2022.
 */
public interface ScientistService {
    String getName();
    Scientist create(Scientist scientist);
}
