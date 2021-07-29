package com.digamber.weather.advice;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.digamber.weather.api.model.ApiProblemDetail;
import com.digamber.weather.exception.ResourceNotFoundException;

/**
 * ControllerAdvice class for {@link com.digamber.weather.api.controllers.WeatherInfoApiController}
 *
 * @author Digamber Gupta
 * @see ControllerAdvice
 */
@ControllerAdvice
public class DefaultControllerAdvice extends ExceptionAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultControllerAdvice.class);

	/**
	 * Handle all unexpected situation
	 *
	 * @param exception any Exception of type {@link Exception}
	 * @param request   http servlet request
	 * @return {@link ResponseEntity} containing the standard body in case of error
	 */
	@ExceptionHandler(Exception.class)
	public HttpEntity<ApiProblemDetail> handleException(Exception exception, final HttpServletRequest request) {
		LOGGER.error("An unexpected error has occurred", exception);

		final ApiProblemDetail problem = new ApiProblemDetail();
		problem.setTitle("Internal Error");
		problem.setDetail("An unexpected error has occurred");
		problem.setInstance(request.getRequestURI());
		problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<>(problem, super.overrideContentType(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handled the {@link ResourceNotFoundException} when resources are not present
	 *
	 * @param exception Resource not found exception
	 * @param request   HttpServletRequest
	 * @return ResponseEntity with standard body in case of error
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public HttpEntity<ApiProblemDetail> handleResourceNotFoundException(ResourceNotFoundException exception,
			HttpServletRequest request) {
		LOGGER.debug("Resource {} of type {} cannot be found", exception.getResourceId(), exception.getResourceType());

		final ApiProblemDetail problem = new ApiProblemDetail();
		problem.setTitle("Resource not found");
		problem.setDetail("Requested resource '" + exception.getResourceId() + "' not found");
		problem.setType(exception.getResourceType());
		problem.setInstance(request.getRequestURI());
		problem.setStatus(HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<>(problem, super.overrideContentType(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles a case when validation of the request body fails
	 *
	 * @param exception any exception of type {@link MethodArgumentNotValidException}
	 * @return {@link ResponseEntity} containing standard body in case of errors
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public HttpEntity<ApiProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
			final HttpServletRequest request) {

		LOGGER.debug("Request body is invalid: {}", exception.getMessage());

		final ApiProblemDetail problem = new ApiProblemDetail();
		problem.setDetail("Validation failed");
		problem.setStatus(HttpStatus.BAD_REQUEST.value());
		problem.setInstance(request.getRequestURI());

		problem.setErrors(exception.getBindingResult().getAllErrors().stream()
				.map(objectError -> {
					ApiProblemDetail problemDetail = new ApiProblemDetail();
					problemDetail.detail("Invalid Parameter");
					problemDetail.setDetail(super.wrapWithFieldName(objectError));
					return problemDetail;
				})
				.collect(Collectors.toList()));

		return new ResponseEntity<>(problem, super.overrideContentType(), HttpStatus.BAD_REQUEST);
	}

}