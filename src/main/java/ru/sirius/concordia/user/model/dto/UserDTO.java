package ru.sirius.concordia.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.user.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    protected Long id;

    protected String nickname;

    protected String name;

    protected String password;

    protected String email;

    protected String phone;

    protected String description;

    protected Integer age;

    protected Boolean isActive;

    protected Role.Code roleCode;

    protected Long cityId;
}