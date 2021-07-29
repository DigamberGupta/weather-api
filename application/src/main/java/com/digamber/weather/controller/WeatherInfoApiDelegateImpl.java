package com.digamber.weather.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.digamber.weather.api.controllers.WeatherInfoApiDelegate;
import com.digamber.weather.api.model.ApiWeatherByCityResponse;
import com.digamber.weather.service.WeatherInfoService;

@Component
public class WeatherInfoApiDelegateImpl implements WeatherInfoApiDelegate {

	private final WeatherInfoService weatherInfoServiceImpl;

	public WeatherInfoApiDelegateImpl(final WeatherInfoService weatherInfoServiceImpl) {
		this.weatherInfoServiceImpl = weatherInfoServiceImpl;
	}

	@Override
	public ResponseEntity<ApiWeatherByCityResponse> getWeatherInfoByCity(String cityName) {

		return ResponseEntity.ok(weatherInfoServiceImpl.getWeatherInfoByCityName(cityName));
	}
}
