package ru.dlabs.sas.example.jsso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * <div><strong>Project name:</strong> dlabs-projects</div>
 * <div><strong>Creation date:</strong> 2024-05-05</div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class ReferenceDto<T> {

    private T value;
    private String title;
}
