package com.example.animelistinanotherworld.jwt;

import com.example.animelistinanotherworld.jwt.JwTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwTAuthenticationFilter jwtAuthFilter;
    private  final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf().disable().authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/register**","/authenticate**","/index**","/login**","/search**").permitAll()
                .requestMatchers("/forgot**","/reset**","/resetPassword**","/forgotPassword**","/signUp").permitAll()
                .requestMatchers("/profile**","/add**","/randomAnime**","/wholeList**","/deleteAnimeFromList**").permitAll()
                .requestMatchers("/planList**","/watchedList**","/watchList**","/getSelectedAnime/**").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authenticationProvider(authenticationProvider);


        return http.build();

    }

}
