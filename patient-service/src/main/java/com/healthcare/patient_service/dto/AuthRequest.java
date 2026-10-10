package com.healthcare.patient_service.dto;

public class AuthRequest {
// WHY a DTO (Data Transfer Object)?
// Your AppUser entity has id, password hash, createdAt, isActive — internal fields.
// The client should send ONLY username and password.
// A DTO is a "form" that defines EXACTLY what the client can send.
// SECURITY: Without a DTO, a hacker could send {"role": "ADMIN"} in the request
//           and Spring would auto-bind it to your entity — instant privilege escalation!

    private String username;
    private String password;

    public String getUsername(){return this.username =  username;}
    public void setUsername(){this.username = username;}

    public String getPassword(){return this.password = password;}
    public void setPassword(String password){ this.password = password;}
}
