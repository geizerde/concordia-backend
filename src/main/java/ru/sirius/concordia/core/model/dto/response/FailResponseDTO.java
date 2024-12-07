package ru.sirius.concordia.core.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FailResponseDTO extends AbstractResponseDTO {
    private final String message;

    private FailResponseDTO(String message) {
        super(ResponseDTOInterface.FAILURE);
        this.message = message;
    }
}
