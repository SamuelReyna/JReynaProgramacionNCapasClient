package com.programacionNCapas.SReynaProgramacionNCapas.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConf {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/usuario").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/usuario/cargamasiva").hasRole("ADMIN")
                .requestMatchers("/usuario/form").hasRole("ADMIN")
                .requestMatchers("/usuario/encriptar").hasRole("ADMIN")
                .requestMatchers("/login").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated())
                .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/usuario", true).permitAll()).logout(logout -> logout.permitAll());
        return httpSecurity.build();
    }

}
