package com.digamber.weather.exception;

import lombok.Getter;

/**
 * This class is used by the service layer when it throw the {@link ResourceNotFoundException}
 *
 * @author Digamber Gupta
 * @see RuntimeException
 */
@SuppressWarnings("unused")
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final Object resourceId;

    private final String resourceType;

    /**
     * Initializing the new instance of {@link ResourceNotFoundException}
     *
     * @param resourceType type of resource which is not found
     * @param resourceId   an identifier of the resource which is not found
     */
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    /**
     * Initializing the new instance of {@link ResourceNotFoundException}
     *
     * @param cause        original cause of the error
     * @param resourceType type of the resource which is not found
     * @param resourceId   an identifier of the resource which is not found
     */
    public ResourceNotFoundException(Throwable cause, String resourceType, Object resourceId) {
        super(cause);
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    /**
     * Initializing the new instance of {@link ResourceNotFoundException}
     *
     * @param message      custom message
     * @param cause        original cause of the error
     * @param resourceType type of the resource which is not found
     * @param resourceId   an identifier of the resource which is not found
     */
    public ResourceNotFoundException(String message, Throwable cause, String resourceType, Object resourceId) {
        super(message, cause);
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }
}