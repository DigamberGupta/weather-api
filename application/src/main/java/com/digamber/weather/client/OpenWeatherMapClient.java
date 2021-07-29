package com.digamber.weather.client;

import com.digamber.weather.client.model.ClientWeatherResponse;

public interface OpenWeatherMapClient {

	ClientWeatherResponse getWeatherInfoByCity(final String cityName);

}
