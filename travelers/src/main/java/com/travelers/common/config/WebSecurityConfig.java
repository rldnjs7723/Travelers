package com.travelers.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.travelers.common.filter.JwtAuthenticationFilter;
import com.travelers.common.handler.WebAccessDeniedHandler;
import com.travelers.common.handler.WebAuthenticationEntryPoint;
import com.travelers.common.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
 
    private final JwtTokenProvider jwtTokenProvider;
    private final WebAccessDeniedHandler webAccessDeniedHandler;
    private final WebAuthenticationEntryPoint webAuthenticationEntryPoint;
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
    		// 기본 보안 금지
            .httpBasic().disable()
            .csrf().disable()
            // 세션 금지
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // 로그인, 회원 가입 관련 요청 허용
                .antMatchers("/user/login/**", "/user/*Check/**", "/user/refresh/**", "/user/find/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user").permitAll()

//            .antMatchers("/", "/js/**", "/assets/**").permitAll()
//            .antMatchers("/user/**", "/", "/js/**", "/assets/**").permitAll()

//                .antMatchers("/article/**").permitAll()
            // Swagger 허용
            .antMatchers("/v2/api-docs", "/swagger*/**").permitAll()
//            .antMatchers("/**").permitAll()
//            .antMatchers("/user/test").hasRole("USER")
            .anyRequest().authenticated()
            // 핸들러 처리
            .and()
            .exceptionHandling()
            	.authenticationEntryPoint(webAuthenticationEntryPoint)
            	.accessDeniedHandler(webAccessDeniedHandler)
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            ;
        return http.build();
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}