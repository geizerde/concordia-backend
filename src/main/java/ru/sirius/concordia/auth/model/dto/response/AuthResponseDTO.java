package ru.sirius.concordia.auth.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
    private final String accessToken;
}
