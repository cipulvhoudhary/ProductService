package com.flopkart.productservice.config;

import com.flopkart.productservice.security.ProductAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ProductAuthenticationInterceptor productAuthenticationInterceptor;

    public WebMvcConfig(ProductAuthenticationInterceptor productAuthenticationInterceptor) {
        this.productAuthenticationInterceptor = productAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(productAuthenticationInterceptor)
                .addPathPatterns("/products/**");
    }
}
