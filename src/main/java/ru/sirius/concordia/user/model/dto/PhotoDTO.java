package ru.sirius.concordia.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    private Long id;

    private String path;

    private Boolean isAvatar;

    private Long userId;
}
