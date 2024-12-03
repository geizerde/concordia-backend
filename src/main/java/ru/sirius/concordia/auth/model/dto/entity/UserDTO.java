package ru.sirius.concordia.auth.model.dto.entity;

import ru.sirius.concordia.auth.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    protected Long id;

    protected String nickname;

    protected String password;

    protected Role.Code roleCode;
}
