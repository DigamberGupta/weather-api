package com.digamber.weather.converter;

import java.math.BigDecimal;
import java.util.Optional;

import com.digamber.weather.api.model.ApiWeatherByCityResponse;
import com.digamber.weather.api.model.ApiWeatherCondition;
import com.digamber.weather.api.model.ApiWeatherProperties;
import com.digamber.weather.api.model.ApiWeatherTemperature;
import com.digamber.weather.api.model.ApiWeatherWindSpeed;
import com.digamber.weather.client.model.ClientWeatherResponse;
import com.digamber.weather.client.model.Weather;

/**
 * Data converter class which convert the client weather data to Api weather data
 *
 * @author Digamber Gupta
 */
public class WeatherDataConverter {

    public static final String WEATHER_TITLE = "Weather";

    private WeatherDataConverter() {
    }

    /**
     * convert and Join method for weather data
     *
     * @param response client api weather response data
     * @return optional of ApiWeatherByCityResponse
     */
    public static Optional<ApiWeatherByCityResponse> convertAndJoin(final ClientWeatherResponse response) {

        if (response == null) {
            return Optional.empty();
        }

        if (response.getWeather().stream().findFirst().isEmpty()) {
            return Optional.empty();
        }

        final Weather weather = response.getWeather().get(0);

        final ApiWeatherCondition condition = new ApiWeatherCondition();
        condition.setType(weather.getDescription());
        condition.setDescription("The description of the weather.");

        final ApiWeatherTemperature temperature = new ApiWeatherTemperature();
        temperature.setType(BigDecimal.valueOf(response.getMain().getTemp()));
        temperature.setDescription("The actual temperature of the city in celsius.");

        final ApiWeatherWindSpeed windSpeed = new ApiWeatherWindSpeed();
        windSpeed.setType(convertMeterPerSecToKmPerHour(response.getWind().getSpeed()));
        windSpeed.setMinimum(0);
        windSpeed.setDescription("Speed of the wind in km/h.");

        final ApiWeatherProperties apiWeatherProperties = new ApiWeatherProperties();
        apiWeatherProperties.setCondition(condition);
        apiWeatherProperties.setTemperature(temperature);
        apiWeatherProperties.setWindSpeed(windSpeed);

        final ApiWeatherByCityResponse apiWeatherByCityResponse = new ApiWeatherByCityResponse();
        apiWeatherByCityResponse.setProperties(apiWeatherProperties);
        apiWeatherByCityResponse.setTitle(WEATHER_TITLE);
        apiWeatherByCityResponse.setType(response.getName());

        return Optional.of(apiWeatherByCityResponse);
    }

    public static BigDecimal convertMeterPerSecToKmPerHour(double valueInMps) {
        return BigDecimal.valueOf(valueInMps * 3.6);
    }

}