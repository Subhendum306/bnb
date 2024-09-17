package com.bnb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
@Configuration
public class SecurityConfig {
    @Autowired
    private JWTFilter jwtFilter;
    @Autowired
    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {
        //h(cd)^2
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
        //haap
        http.authorizeHttpRequests().anyRequest().permitAll();
     /*   http.authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/createuser","/api/v1/auth/createpropertyowner","/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/property/addProperty").hasAnyRole("OWNER","ADMIN","MANAGER")
                .requestMatchers("/api/v1/property/createpropertymanager").hasRole("ADMIN")
                .anyRequest().authenticated();
*/

        return http.build();
    }
}

