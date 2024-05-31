package ru.dlabs.sas.example.jsso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.dlabs.sas.example.jsso.service.CustomOAuth2UserService;
import ru.dlabs.sas.example.jsso.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity(debug = true)
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService userDetailService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomUserDetailsService userDetailService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        SocialConfigurer socialConfigurer = new SocialConfigurer();
        socialConfigurer.setoAuth2UserService(customOAuth2UserService);
        http.apply(socialConfigurer);

        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService);

        http.authorizeHttpRequests(authorize ->
                authorize.anyRequest().authenticated()
        );
        return http.formLogin(withDefaults()).build();
    }
}
