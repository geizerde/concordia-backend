package ru.sirius.concordia.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.user.model.dto.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {

    private UserDTO sender;

    private UserDTO receiver;

    private Boolean like;
}

