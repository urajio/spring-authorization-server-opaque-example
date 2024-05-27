package ru.dlabs.sas.example.jsso.config.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import ru.dlabs.sas.example.jsso.exception.AuthException;
import ru.dlabs.sas.example.jsso.type.AuthErrorCode;

@Slf4j
public class CustomOauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public CustomOauth2AuthenticationFailureHandler(String locationUrl) {
        this.setDefaultFailureUrl(locationUrl);
        this.setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
                String redirectUrl = this.calculateRedirectUrl(request.getContextPath(), url);
                redirectUrl = this.addErrorParams(redirectUrl, request);
                redirectUrl = response.encodeRedirectURL(redirectUrl);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(LogMessage.format("Redirecting to %s", redirectUrl));
                }

                response.sendRedirect(redirectUrl);
            }

            private String addErrorParams(String redirectUrl, HttpServletRequest request) {
                AuthenticationException authenticationException = getAuthenticationException(request);
                if (authenticationException == null) {
                    return redirectUrl;
                }
                redirectUrl += "?";
                if (authenticationException instanceof AuthException authException) {
                    redirectUrl += "error_code=" + authException.getErrorCode().name();
                } else {
                    redirectUrl += "error_code="
                            + AuthErrorCode.AUTH_ERROR.name()
                            + "&error_desc="
                            + this.encodeValue(authenticationException.getLocalizedMessage());
                }

                return redirectUrl;
            }

            @SneakyThrows
            private String encodeValue(String value) {
                return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
            }
        });
    }

    private AuthenticationException getAuthenticationException(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
        return null;
    }


}
