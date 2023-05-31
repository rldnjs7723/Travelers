package com.travelers.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.travelers.common.interceptor.AuthenticationInterceptor;
import com.travelers.common.interceptor.ScheduleInterceptor;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final AuthenticationInterceptor confirmInterceptor;
	private final ScheduleInterceptor scheduleInterceptor;
	private final List<String> patterns = Arrays.asList("/article/**", "/mypage", "/search", "/schedule", "/hotplace");

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(confirmInterceptor).addPathPatterns(patterns);
		registry.addInterceptor(scheduleInterceptor).addPathPatterns("/schedule", "/article/schedule/**", "/attraction/schedule/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
//		.allowedOrigins("http://localhost:8080", "http://localhost:8081")
			.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
					HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
					HttpMethod.PATCH.name())
			.maxAge(1800);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/hotplace").setViewName("/article/hotplaceWrite");
	}
}
