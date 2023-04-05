package com.example.Todo.config;
// 스프링 시큐리티에 사용할 환경 설정 파일
// 스프링 시큐리티에 JwtAuthenticationFilter 사용울 설정

import org.springframework.web.filter.CorsFilter;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


import com.example.Todo.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    @Autowired
    public WebSecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/","/auth/**","/h2-console/**").permitAll()
                .anyRequest()
                .authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    Map<String,Object> data = new HashMap<String, Object>();
                    data.put("status", HttpServletResponse.SC_FORBIDDEN);
                    data.put("message", e.getMessage());

                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    objectMapper.writeValue(response.getOutputStream(), data);

                });
       /*
       http.exceptionHandling()
       .authenticationEntryPoint((request,response,e) ->
       {
           response.setContentType("application/json;charset=UTF-8");
           response.setStatus(HttpServletResponse.SC_FORBIDDEN);

           String message;
        if(e.getCause() != null) {
        message = e.getCause().getMessage();
        } else {
        message = e.getMessage();
        }
        byte[] body = new ObjectMapper()
        .writeValueAsBytes(Collections.singletonMap("status", "403"));
        response.getOutputStream().write(body);


       });
       */

        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);

    }
}