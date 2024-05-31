package ru.dlabs.sas.example.jsso.dto;

import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public class IntrospectionPrincipal {

    private UUID id;
    private String firstName;
    private String secondName;
    private String middleName;
    private LocalDate birthday;
    private String avatarUrl;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public IntrospectionPrincipal(UUID id, String firstName, String secondName, String middleName, LocalDate birthday, String avatarUrl, String username, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
        this.birthday = birthday;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    public static IntrospectionPrincipal build(AuthorizedUser authorizedUser) {
        return new IntrospectionPrincipal(
                authorizedUser.getId(),
                authorizedUser.getFirstName(),
                authorizedUser.getSecondName(),
                authorizedUser.getMiddleName(),
                authorizedUser.getBirthday(),
                authorizedUser.getAvatarUrl(),
                authorizedUser.getUsername(),
                authorizedUser.getEmail(),
                authorizedUser.getAuthorities());
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
