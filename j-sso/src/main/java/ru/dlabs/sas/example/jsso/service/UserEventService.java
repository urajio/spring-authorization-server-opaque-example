package ru.dlabs.sas.example.jsso.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.dlabs.sas.example.jsso.dao.type.UserEventType;
import ru.dlabs.sas.example.jsso.dto.PageableResponseDto;
import ru.dlabs.sas.example.jsso.dto.UserEventDto;

/**
 * <p>
 * <div><strong>Project name:</strong> dlabs-projects</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface UserEventService {

    PageableResponseDto<UserEventDto> searchEvents(int page, int pageSize);

    void createEvent(UserEventType eventType, String clientId, HttpServletRequest request);

    /**
     * Удалить события пользователя, являющиеся устаревшими.
     */
    void deleteOldEvents();
}
