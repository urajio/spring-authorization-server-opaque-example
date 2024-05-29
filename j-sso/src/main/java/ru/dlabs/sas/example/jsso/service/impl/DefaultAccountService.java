package ru.dlabs.sas.example.jsso.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.dlabs.sas.example.jsso.dao.entity.UserEntity;
import ru.dlabs.sas.example.jsso.dao.repository.UserRepository;
import ru.dlabs.sas.example.jsso.dao.type.StoreType;
import ru.dlabs.sas.example.jsso.dao.type.UserEventType;
import ru.dlabs.sas.example.jsso.dto.AuthorizedUser;
import ru.dlabs.sas.example.jsso.dto.FileStoreDto;
import ru.dlabs.sas.example.jsso.dto.UserDto;
import ru.dlabs.sas.example.jsso.exception.ServiceException;
import ru.dlabs.sas.example.jsso.mapper.AuthorizedUserMapper;
import ru.dlabs.sas.example.jsso.mapper.UserDtoMapper;
import ru.dlabs.sas.example.jsso.service.AccountService;
import ru.dlabs.sas.example.jsso.service.FileStoreService;
import ru.dlabs.sas.example.jsso.service.MessageService;
import ru.dlabs.sas.example.jsso.service.UserClientService;
import ru.dlabs.sas.example.jsso.service.UserEventService;
import ru.dlabs.sas.example.jsso.service.UserTokenService;
import ru.dlabs.sas.example.jsso.service.security.SecurityService;
import ru.dlabs.sas.example.jsso.utils.HttpUtils;
import ru.dlabs.sas.example.jsso.utils.SecurityUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAccountService implements AccountService {

    private final UserRepository userRepository;
    private final FileStoreService fileStoreService;
    private final SecurityService securityService;
    private final UserTokenService userTokenService;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;
    private final UserClientService userClientService;
    private final UserEventService eventService;
    private final MessageService messageService;


    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        UserEntity entity = userRepository.getReferenceById(authorizedUser.getId());
        return UserDtoMapper.map(entity);
    }

    @Override
    @Transactional
    public UserDto save(UserDto dto, MultipartFile avatarFile) {
        Optional<UserEntity> entityWrapper = userRepository.findById(dto.getId());
        if (entityWrapper.isEmpty()) {
            throw ServiceException.builder("Entity not found").build();
        }
        UserEntity entity = entityWrapper.get();

        if (avatarFile != null && !avatarFile.isEmpty()) {
            FileStoreDto fileStoreDto = fileStoreService.saveOrReplace(
                avatarFile,
                StoreType.AVATAR,
                entity.getAvatarFileId()
            );
            entity.setAvatarFileId(fileStoreDto != null ? fileStoreDto.getId() : null);
        }

        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setBirthday(dto.getBirthday());
        entity = userRepository.save(entity);

        AuthorizedUser updatedAuthorizedUser = AuthorizedUserMapper.reload(SecurityUtils.getAuthUser(), entity);
        securityService.reloadSecurityContext(updatedAuthorizedUser);
        return UserDtoMapper.map(entity);
    }

    @Override
    @Transactional
    public void deleteCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userTokenService.recallAllCurrentUserTokens();
        userClientService.markAsDelete(authorizedUser.getId());
        fileStoreService.delete(authorizedUser.getAvatarFileId());
        userRepository.deleteById(authorizedUser.getId());
        eventService.createEvent(UserEventType.USER_DELETE, null, request);
        securityContextLogoutHandler.logout(request, response, authentication);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getAvatarCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        if (authorizedUser.getAvatarFileId() == null) {
            return null;
        }
        FileStoreDto fileStoreDto = fileStoreService.getById(authorizedUser.getAvatarFileId());
        if (fileStoreDto == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            byte[] fileByteArray = fileStoreService.download(authorizedUser.getAvatarFileId());
            return HttpUtils.appendFileToResponse(fileStoreDto.getName(), fileStoreDto.getContentType(), fileByteArray);
        } catch (IOException | RuntimeException e) {
            log.error(e.getMessage(), e);
            throw ServiceException.builder(
                messageService.getMessage("file.store.not.found", fileStoreDto.getName())
            ).build();
        }
    }
}
