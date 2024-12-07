package ru.sirius.concordia.core.model.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class AbstractResponseDTO implements ResponseDTOInterface {
    private final boolean status;

    protected AbstractResponseDTO(boolean status) {
        this.status = status;
    }

    @Override
    public boolean getStatus() {
        return status;
    }
}
