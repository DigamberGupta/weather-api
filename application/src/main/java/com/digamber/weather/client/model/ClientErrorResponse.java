package com.digamber.weather.client.model;

import lombok.Data;

@Data
public class ClientErrorResponse {
	private String cod;
	private String message;
}
