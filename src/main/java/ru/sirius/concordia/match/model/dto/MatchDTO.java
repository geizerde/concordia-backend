package ru.sirius.concordia.match.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.user.model.dto.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {

    @JsonIgnore
    private UserDTO sender;

    private UserDTO receiver;

    @JsonProperty("is_liked")
    private Boolean isLiked;
}

