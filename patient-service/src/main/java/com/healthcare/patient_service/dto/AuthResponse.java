package com.healthcare.patient_service.dto;

public class AuthResponse {

    private String token;

    public AuthResponse(String token){
        this.token = token;
    }

    private String getToken(){return token;}

}
