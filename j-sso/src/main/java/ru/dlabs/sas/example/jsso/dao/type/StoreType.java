package ru.dlabs.sas.example.jsso.dao.type;

import lombok.Getter;

/**
 * Тип (папка) раздела файлового хранилища.
 * <p>
 * <div><strong>Project name:</strong> dlabs-projects</div>
 * <div><strong>Creation date:</strong> 2024-05-09</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
public enum StoreType {
    AVATAR("avatars");

    /**
     * Имя раздела (папки)
     **/
    private final String bucket;

    StoreType(String bucket) {
        this.bucket = bucket;
    }
}
