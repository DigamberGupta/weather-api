package com.digamber.weather.service;

import static com.digamber.weather.converter.WeatherDataConverter.convertMeterPerSecToKmPerHour;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import com.digamber.weather.api.model.ApiWeatherByCityResponse;
import com.digamber.weather.client.OpenWeatherMapClient;
import com.digamber.weather.client.model.ClientWeatherResponse;
import com.digamber.weather.converter.WeatherDataConverter;
import com.digamber.weather.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

class WeatherInfoServiceImplTest {

	private WeatherInfoService service;

	private OpenWeatherMapClient clientMock;

	@BeforeEach
	void setUp() {
		clientMock = Mockito.mock(OpenWeatherMapClient.class);
		service = new WeatherInfoServiceImpl(clientMock);
	}

	@AfterEach
	void tearDown() {
		service = null;
		clientMock = null;
	}

	@ParameterizedTest
	@ValueSource(strings = { "openweathermap-sample.json" })
	void getWeatherInfoByCityName(final String clientResponseJsonFileName) throws IOException {
		//Data
		final ClientWeatherResponse sampleClientResponse = getSampleClientResponse(clientResponseJsonFileName);

		//Mock
		when(clientMock.getWeatherInfoByCity(Mockito.anyString())).thenReturn(sampleClientResponse);

		//Call
		final ApiWeatherByCityResponse apiResponse = service.getWeatherInfoByCityName("Munich");

		//assertion
		assertNotNull(apiResponse);
		assertEquals(WeatherDataConverter.WEATHER_TITLE, apiResponse.getTitle());
		assertEquals(sampleClientResponse.getName(), apiResponse.getType());

		assertNotNull(apiResponse.getProperties());

		assertNotNull(apiResponse.getProperties().getCondition());
		assertEquals(sampleClientResponse.getWeather().get(0).getDescription(), apiResponse.getProperties().getCondition().getType());
		assertNotNull(apiResponse.getProperties().getCondition().getDescription());

		assertNotNull(apiResponse.getProperties().getTemperature());
		assertEquals(sampleClientResponse.getMain().getTemp(), apiResponse.getProperties().getTemperature().getType().doubleValue());
		assertNotNull(apiResponse.getProperties().getTemperature().getDescription());

		assertNotNull(apiResponse.getProperties().getWindSpeed());
		assertEquals(convertMeterPerSecToKmPerHour(sampleClientResponse.getWind().getSpeed()), apiResponse.getProperties().getWindSpeed().getType());
		assertNotNull(apiResponse.getProperties().getWindSpeed().getDescription());
	}

	@Test
	void getWeatherInfoByCityName_WHEN_RESPONSE_IS_NULL() {

		//Mock
		when(clientMock.getWeatherInfoByCity(Mockito.anyString())).thenReturn(null);

		final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
				() -> service.getWeatherInfoByCityName("Munich"));

		//assertion
		assertNotNull(resourceNotFoundException);
	}

	@Test
	void getWeatherInfoByCityName_WHEN_RESOURCE_NOT_FOUND() {

		//Mock
		when(clientMock.getWeatherInfoByCity(Mockito.anyString())).thenThrow(new ResourceNotFoundException("cityName", new Object()));

		final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
				() -> service.getWeatherInfoByCityName("Munich"));

		//assertion
		assertNotNull(resourceNotFoundException);
	}

	private ClientWeatherResponse getSampleClientResponse(final String clientResponseJsonFileName) throws IOException {
		final URL resource = WeatherInfoServiceImplTest.class.getResource("/" + clientResponseJsonFileName);
		assert resource != null;
		return new ObjectMapper().readValue(new File(resource.getFile()), ClientWeatherResponse.class);
	}
}