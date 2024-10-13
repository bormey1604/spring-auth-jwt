package com.techgirl.spring_auth_jwt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private String statusCode;
    private String message;
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> data;


    public AuthResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public AuthResponse(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = null;
    }


    public AuthResponse(String statusCode, String message, Map<String, Object> data) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

}
