package ru.sirius.concordia.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.user.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Long id;

    private String name;

    private Role.Code code;
}
