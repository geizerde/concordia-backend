package ru.sirius.concordia.core.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public abstract class AbstractResponseDTO implements ResponseDTOInterface {
    protected final boolean status;

    @Override
    public boolean getStatus() {
        return status;
    }
}
