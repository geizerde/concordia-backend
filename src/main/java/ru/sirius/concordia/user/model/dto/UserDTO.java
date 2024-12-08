package ru.sirius.concordia.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.user.model.Role;
import ru.sirius.concordia.user.model.location.City;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    protected Long id;

    protected String nickname;

    protected String password;

    protected Role.Code roleCode;

    protected Long cityId;
}
