package com.sita.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseStatsDto {

    @JsonProperty("HP")
    private int hp;

    @JsonProperty("Attack")
    private int attack;

    @JsonProperty("Defense")
    private int defense;

    @JsonProperty("Sp. Attack")
    private int spAttack;

    @JsonProperty("Sp. Defense")
    private int spDefense;

    @JsonProperty("Speed")
    private int speed;

    // Getters + setters...
}
