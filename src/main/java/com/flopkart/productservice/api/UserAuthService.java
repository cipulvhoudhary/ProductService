package com.flopkart.productservice.api;

import com.flopkart.productservice.dtos.UserDto;
import com.flopkart.productservice.exceptions.UnauthorisedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserAuthService {
    RestTemplate restTemplate;

    public UserAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto validateToken(String tokenValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("tokenValue", tokenValue);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                "http://localhost:8080/auth/validate",
                HttpMethod.POST,
                request,
                UserDto.class
        );

        return responseEntity.getBody();
    }
}
