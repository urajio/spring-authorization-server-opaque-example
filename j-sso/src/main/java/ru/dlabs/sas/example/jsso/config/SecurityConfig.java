package ru.dlabs.sas.example.jsso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import ru.dlabs.sas.example.jsso.service.CustomOAuth2UserService;
import ru.dlabs.sas.example.jsso.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService userDetailService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomUserDetailsService userDetailService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.userDetailService = userDetailService;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Spring Security Debugger can be enabled here
        return webSecurity -> webSecurity.debug(false);
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(customizer -> customizer.userService(customOAuth2UserService)))
                .formLogin(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated())
                .userDetailsService(userDetailService);
        return http.build();
    }
}
