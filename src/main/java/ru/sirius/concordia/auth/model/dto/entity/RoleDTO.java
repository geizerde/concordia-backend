package ru.sirius.concordia.auth.model.dto.entity;

import ru.sirius.concordia.auth.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Long id;

    private String name;

    private Role.Code code;
}
