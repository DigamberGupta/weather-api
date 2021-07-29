package com.digamber.weather.client.model;

import lombok.Data;

@Data
public class Sys{
    private int type;
    private int id;
    private String country;
    private int sunrise;
    private int sunset;
}