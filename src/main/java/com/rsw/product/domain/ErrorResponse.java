package com.rsw.product.domain;

/**
 * Created by DAlms on 12/18/16.
 */
public class ErrorResponse {
    private String error;
    private String description;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
