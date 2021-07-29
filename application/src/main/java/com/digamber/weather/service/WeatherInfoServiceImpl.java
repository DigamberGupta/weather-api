package com.digamber.weather.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.digamber.weather.api.model.ApiWeatherByCityResponse;
import com.digamber.weather.client.OpenWeatherMapClient;
import com.digamber.weather.converter.WeatherDataConverter;
import com.digamber.weather.exception.ResourceNotFoundException;

@Service
public class WeatherInfoServiceImpl implements WeatherInfoService {

	private final OpenWeatherMapClient client;

	public WeatherInfoServiceImpl(final OpenWeatherMapClient openWeatherMapClient) {
		this.client = openWeatherMapClient;
	}

	@Cacheable("weather")
	public ApiWeatherByCityResponse getWeatherInfoByCityName(final String cityName) {

		return WeatherDataConverter.convertAndJoin(client.getWeatherInfoByCity(cityName))
				.orElseThrow(() -> new ResourceNotFoundException("cityName", cityName));
	}

}
