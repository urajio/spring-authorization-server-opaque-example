package ru.dlabs.sas.example.jsso.mapper;

import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.type.AuthProvider;

import java.util.Collections;

public class AuthorizedUserMapper {

    public static AuthorizedUser map(UserEntity entity, AuthProvider provider) {
        return AuthorizedUser.builder(entity.getEmail(), entity.getPasswordHash(), Collections.emptyList())
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .middleName(entity.getMiddleName())
                .birthday(entity.getBirthday())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }
}
