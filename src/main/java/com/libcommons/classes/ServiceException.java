package com.libcommons.classes;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {
    private HttpStatus status;
    private String controllerName;
    private String methodName;

    public ServiceException(String message, String controllerName, String methodName, HttpStatus status) {
        super(message);
        this.status = status;
        this.controllerName = controllerName;
        this.methodName = methodName;
    }

    public String getDetails() {
        return String.format("%s -> /%s", this.controllerName, this.methodName);
    }
}
