package com.digamber.weather.swagger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Customer controller to provide open-api specification with external swagger-ui
 *
 * @author digamber.gupta
 */
@RestController
public class ApiDocsController {
	private static final Logger logger = LoggerFactory.getLogger(ApiDocsController.class);

	private static final String API_RESOURCE_ROOT = "/api-docs";

	@Autowired
	private ApiDocsProperties properties;

	@CrossOrigin
	@GetMapping(value = "/api-docs/")
	public ResponseEntity<?> index(HttpServletRequest request) {
		StringBuilder buf = new StringBuilder();
		buf.append("<html>");
		buf.append("<title>").append(properties.getTitle()).append("</title>");
		buf.append("<body>");

		buf.append("<h1>").append(properties.getTitle()).append("</h1>");

		for (String entry : properties.getIndex()) {
			URI entryURI = buildEntryURI(request, entry);
			buf.append("<div>");
			buf.append("<a href=\"");
			String uri = UriComponentsBuilder.fromUri(properties.getSwaggerUiUrl()) //
					.queryParam("url", entryURI) //
					.build() //
					.toUriString();
			buf.append(uri);
			buf.append("\">");
			buf.append(entry);
			buf.append("</a>");
			buf.append("</div>");
		}

		buf.append("</html>");
		buf.append("</body>");

		return ResponseEntity.status(HttpStatus.OK) //
				.contentType(MediaType.TEXT_HTML) //
				.body(buf.toString());
	}

	private URI buildEntryURI(HttpServletRequest request, String entry) {
		return UriComponentsBuilder.fromUri(buildHostBaseURI(request)) //
				.pathSegment("api-docs") //
				.pathSegment(entry) //
				.build() //
				.toUri();
	}

	private URI buildHostBaseURI(HttpServletRequest request) {
		return UriComponentsBuilder.fromUriString(request.getRequestURL().toString()) //
				.replacePath(null) //
				.scheme(properties.isForceHttps() ? "https" : "http") //
				.build() //
				.toUri();
	}

	@CrossOrigin
	@GetMapping(value = "/api-docs/{resource}")
	public ResponseEntity<?> getResource(HttpServletRequest request, @PathVariable("resource") String resourceName) {
		String content = getContent(request, resourceName);

		if (content == null) {
			return ResponseEntity.notFound().build();
		}

		MediaType mediaType = mediaTypeOf(resourceName);

		return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(content);
	}

	private MediaType mediaTypeOf(String resourceName) {
		if (resourceName.endsWith(".yaml")) {
			return MediaType.valueOf("text/yaml");
		} else if (resourceName.endsWith(".json")) {
			return MediaType.APPLICATION_JSON;
		} else {
			return MediaType.TEXT_PLAIN;
		}
	}

	public String getContent(HttpServletRequest request, String name) {
		try {
			try (InputStream raw = getInputStream(name.concat(".yaml"))) {
				if (name.endsWith(".yaml")) {
					return processYamlFile(request, raw);
				} else {
					return readAll(raw);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private String processYamlFile(HttpServletRequest request, InputStream raw) {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(options);
		Map content = yaml.load(raw);

		if (content.containsKey("servers")) {
			URI uri = buildHostBaseURI(request);
			content.remove("servers");
			content.put("servers", Collections.singletonList(Collections.singletonMap("url", uri.toString())));
		}

		processApiNode(content);

		return yaml.dump(content);
	}

	@SuppressWarnings("rawtypes")
	private void processApiNode(Map node) {
		Set keys = new HashSet(node.keySet());
		keys.forEach(key -> {
			Object value = node.get(key);
			if (value instanceof Map) {
				processApiNode((Map) value);
			} else if (key.equals("externalValue")) {
				String name = node.remove(key).toString();

				try (InputStream is = getInputStream(name)) {
					if (is == null) {
						logger.error("No resource " + name);
					} else {
						node.put("value", readAll(is));
					}
				} catch (IOException e) {
					wrapIOException(e);
				}
			}
		});
	}

	public String readAll(InputStream is) throws IOException {
		if (is == null) {
			return null;
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		StreamUtils.copy(is, os);

		return os.toString(StandardCharsets.UTF_8);
	}

	public InputStream getInputStream(String name) throws IOException {
		ClassPathResource resource = new ClassPathResource(API_RESOURCE_ROOT + "/" + name);
		if (!resource.exists()) {
			return null;
		}

		return resource.getInputStream();
	}

	private void wrapIOException(IOException e) {
		throw new RuntimeException(e);
	}
}