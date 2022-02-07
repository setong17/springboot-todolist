package com.springboot.todolist.config;

import com.springboot.todolist.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {

        // http 시큐리티 빌더
        http.cors()
                .and()
                .csrf()
                    .disable()
                .httpBasic()    // token을 사용하므로 basic 인증 disable
                    .disable()
                .sessionManagement()    // session 기반이 아님
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/", "/auth/**").permitAll()
                .anyRequest()
                    .authenticated();

        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );

    }
}
