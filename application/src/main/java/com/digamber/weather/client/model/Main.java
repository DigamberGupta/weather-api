package com.digamber.weather.client.model;

import lombok.Data;

@Data
public class Main{
    public double temp;
    public double feels_like;
    public double temp_min;
    public double temp_max;
    public int pressure;
    public int humidity;
}