package ru.sirius.concordia.auth.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequestDTO {

    private String nickname;

    private String password;
}
