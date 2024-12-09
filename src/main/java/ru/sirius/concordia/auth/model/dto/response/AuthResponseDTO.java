package ru.sirius.concordia.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
    @JsonProperty("access_token")
    private final String accessToken;
}
