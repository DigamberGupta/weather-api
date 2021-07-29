package com.digamber.weather.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.digamber.weather.client.model.ClientWeatherResponse;
import com.digamber.weather.exception.ResourceNotFoundException;

@Component("openWeatherMapClient")
public class OpenWeatherMapClientImpl implements OpenWeatherMapClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherMapClientImpl.class);

	private final String baseUrl;

	private final String apiKey;

	private final RestTemplate restTemplate;

	public OpenWeatherMapClientImpl(@Value("${client.weathermap.baseUrl}") final String baseUrl, //
			@Value("${client.weathermap.api-key}") final String apiKey,
			final RestTemplate restTemplate) {
		this.baseUrl = baseUrl;
		this.apiKey = apiKey;
		this.restTemplate = restTemplate;
	}

	public ClientWeatherResponse getWeatherInfoByCity(final String cityName) {
		if (ObjectUtils.isEmpty(cityName)) {
			throw new IllegalArgumentException();
		}
		try {
			final UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(baseUrl)
					.queryParam("q", cityName)
					.queryParam("appid", apiKey)
					.queryParam("units", "metric");

			return restTemplate.getForEntity(builder.build().toUri(), ClientWeatherResponse.class).getBody();
		} catch (HttpClientErrorException errorException) {
			LOGGER.info("HttpClientError for getWeatherInfoByCity(" + cityName + ")", errorException);

			if (errorException.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ResourceNotFoundException(errorException, "cityName", cityName);
			}

			throw errorException;
		}
	}

}