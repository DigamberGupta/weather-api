# Weather API 
 - It's a spring boot application, which provides the simple way of using third party API for create your custom weather api for your web application.
   example: Get the current weather for the city name using the OpenWeather API
 - This application also provide a way to generate the client and model class using the defined OpenAPI specification YAML.
 - Weather API client also provides a way to expose you OpenAPI specification using an external Swagger UI
 - It provides an example to use Spring boot starter cache


## Third Party API 
Please use the current open weather API https://openweathermap.org/current

## Requirements
- JAVA 11
- Gradle 7.0.2


### Open-API Specification and Swagger UI
- Open API specification http://{host}:{port}/api-docs/weather-api
- To access Swagger UI, visit to http://{host}:{port}/api-docs/ and click on the weather-api link in web browser which will redirect to the public Swagger-UI
  (https://editor.swagger.io/?url=http://{host}:{port}/api-docs/weather-api)
- Return the response as JSON following this schema:
```
{
  "title": "Weather",
  "type": "object",
  "properties": {
    "condition": {
      "type": "string",
      "description": "The description of the weather. eg: scattered clouds."
    },
    "temperature": {
      "type": "number",
      "description": "The actual temperature of the city in celsius."
    },
    "wind_speed": {
      "type": "number",
      "description": "Speed of the wind in km/h.",
      "minimum": 0
    }
  }
}
```
