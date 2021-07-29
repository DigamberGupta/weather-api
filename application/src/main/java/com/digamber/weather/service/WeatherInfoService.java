package com.digamber.weather.service;

import com.digamber.weather.api.model.ApiWeatherByCityResponse;

public interface WeatherInfoService {

	ApiWeatherByCityResponse getWeatherInfoByCityName(String cityName);
}
