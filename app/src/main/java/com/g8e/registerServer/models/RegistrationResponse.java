package com.g8e.registerServer.models;

public class RegistrationResponse {
    private boolean success;
    private String error;

    public RegistrationResponse(boolean success) {
        this.success = success;
    }

    public RegistrationResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

}
