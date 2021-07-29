package com.digamber.weather.swagger;

import java.net.URI;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apidocs", ignoreUnknownFields = false)
public class ApiDocsProperties {

	/**
	 * Title for generated index page.
	 */
	private String title;
	/**
	 * List of resource to include in the index page.
	 */
	private List<String> index;
	/**
	 * If true, generated URL in index page will use https scheme.
	 */
	private boolean forceHttps = true;
	/**
	 * Url for swagger UI to which index page will link.
	 */
	private URI swaggerUiUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getIndex() {
		return index;
	}

	public void setIndex(List<String> index) {
		this.index = index;
	}

	public boolean isForceHttps() {
		return forceHttps;
	}

	public void setForceHttps(boolean forceHttps) {
		this.forceHttps = forceHttps;
	}

	public URI getSwaggerUiUrl() {
		return swaggerUiUrl;
	}

	public void setSwaggerUiUrl(URI swaggerUiUrl) {
		this.swaggerUiUrl = swaggerUiUrl;
	}
}
