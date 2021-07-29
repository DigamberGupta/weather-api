package com.digamber.weather.client.model;

import java.util.List;

import lombok.Data;

@Data
public class ClientWeatherResponse {

	private Coord coord;
	private List<Weather> weather;
	private String base;
	private Main main;
	private int visibility;
	private Wind wind;
	private Rain rain;
	private Clouds clouds;
	private int dt;
	private Sys sys;
	private int timezone;
	private int id;
	private String name;
	private int cod;

}
