package com.dvtt.demo.distributedtracinghandmade.animalname.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by linhtn on 1/3/2022.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Animal {
    private Long id;
    private String createdAt;
    private String name;
    private String avatar;
}
