package com.amitabh.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import static com.amitabh.authentication.security.ApplicationUserPermission.*;
import static com.amitabh.authentication.security.ApplicationUserRole.*;


@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        {
                            try {
                                authorizeHttpRequests
                                        .requestMatchers("/", "index", "/css/*", "/js/*").permitAll()
                                        .requestMatchers("/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                                        .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                                        .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                                        .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                                        .requestMatchers("/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                                        .anyRequest()
                                        .authenticated()
                                        .and()
//                                        .httpBasic();
                                        .formLogin();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails annaSmith = User.builder()
                .username("anna")
                .password(passwordEncoder.encode("12345678"))
//                .roles(STUDENT.name())
                .authorities(STUDENT.getGrantedAuthority())
                .build();

        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("12345678"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthority())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("12345678"))
//                .roles(ADMIN.name())
                .authorities(ADMINTRAINEE.getGrantedAuthority())
                .build();

        return new InMemoryUserDetailsManager(
                annaSmith,
                lindaUser,
                tomUser
        );

    }
}
