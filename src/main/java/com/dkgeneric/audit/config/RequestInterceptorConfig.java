package com.dkgeneric.audit.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dkgeneric.audit.controller.ServiceInterceptor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestInterceptorConfig implements WebMvcConfigurer {
	private final ServiceInterceptor serviceInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(serviceInterceptor);
	}
}
