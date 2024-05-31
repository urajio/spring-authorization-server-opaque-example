package ru.dlabs.sas.example.jsso.dto;

import java.net.URL;
import java.time.Instant;
import java.util.List;

public class TokenInfoDto {

    private Boolean active;
    private String sub;
    private List<String> aud;
    private Instant nbf;
    private List<String> scopes;
    private URL iss;
    private Instant exp;
    private Instant iat;
    private String jti;
    private String clientId;
    private String tokenType;

    private IntrospectionPrincipal principal;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public List<String> getAud() {
        return aud;
    }

    public void setAud(List<String> aud) {
        this.aud = aud;
    }

    public Instant getNbf() {
        return nbf;
    }

    public void setNbf(Instant nbf) {
        this.nbf = nbf;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public URL getIss() {
        return iss;
    }

    public void setIss(URL iss) {
        this.iss = iss;
    }

    public Instant getExp() {
        return exp;
    }

    public void setExp(Instant exp) {
        this.exp = exp;
    }

    public Instant getIat() {
        return iat;
    }

    public void setIat(Instant iat) {
        this.iat = iat;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public IntrospectionPrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(IntrospectionPrincipal principal) {
        this.principal = principal;
    }
}
