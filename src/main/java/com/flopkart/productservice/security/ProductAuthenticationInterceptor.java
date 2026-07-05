package com.flopkart.productservice.security;

import com.flopkart.productservice.api.UserAuthService;
import com.flopkart.productservice.dtos.UserDto;
import com.flopkart.productservice.exceptions.UnauthorisedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ProductAuthenticationInterceptor implements HandlerInterceptor {

    public static final String AUTHENTICATED_USER_ATTRIBUTE = "authenticatedUser";

    private final UserAuthService userAuthService;

    public ProductAuthenticationInterceptor(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tokenValue = request.getHeader("tokenValue");

        if (tokenValue == null || tokenValue.isBlank()) {
            throw new UnauthorisedException("Invalid token");
        }

        try {
            UserDto userDto = userAuthService.validateToken(tokenValue);
            if (userDto == null) {
                throw new UnauthorisedException("User Not Authorised");
            }
            request.setAttribute(AUTHENTICATED_USER_ATTRIBUTE, userDto);
            return true;
        } catch (UnauthorisedException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new UnauthorisedException("Invalid token");
        }
    }
}
