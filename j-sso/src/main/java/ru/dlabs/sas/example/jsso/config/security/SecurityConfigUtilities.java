package ru.dlabs.sas.example.jsso.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import ru.dlabs.sas.example.jsso.dto.AuthorizationInfo;
import ru.dlabs.sas.example.jsso.service.security.RedisOAuth2AuthorizationConsentService;
import ru.dlabs.sas.example.jsso.service.security.RedisOAuth2AuthorizationService;
import ru.dlabs.sas.example.jsso.service.UserClientService;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfigUtilities {

    private final AuthorizationServerProperties authorizationServerProperties;
    private final UserClientService userClientService;

    @Bean
    public RedisTemplate<String, OAuth2Authorization> redisTemplateOAuth2Authorization(
        RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, OAuth2Authorization> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, AuthorizationInfo> redisTemplateAuthInfo(
        RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, AuthorizationInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplateOAuth2AuthorizationConsent(
        RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService(
        RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate
    ) {
        return new RedisOAuth2AuthorizationConsentService(
            redisTemplate,
            authorizationServerProperties.getAuthorizationTtl()
        );
    }

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(
        RedisTemplate<String, OAuth2Authorization> redisTemplate,
        RedisTemplate<String, AuthorizationInfo> redisTemplateAuthInfo
    ) {
        return new RedisOAuth2AuthorizationService(
            redisTemplate,
            redisTemplateAuthInfo,
            (authInfo) -> userClientService.save(authInfo.getUserId(), authInfo.getClientId()),
            (authorization) -> {
            },
            authorizationServerProperties.getAuthorizationTtl()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
