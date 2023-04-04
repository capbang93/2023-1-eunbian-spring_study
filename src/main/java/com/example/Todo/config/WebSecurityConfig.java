package com.example.Todo.config;

import com.example.Todo.security.JwtAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

// 스프링 시큐리티에 사용할 환경 설정 파일
// 스프링 시큐리티에 JwtAuthenticationFilter 사용울 설정
@EnableWebSecurity // Spring Security 설정 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
      protected void configure(HttpSecurity http) throws Exception{
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
                .antMatchers("/","/auth/**").permitAll()
                .anyRequest()
                .authenticated();

                http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
    }
}
