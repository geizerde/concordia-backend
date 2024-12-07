package ru.sirius.concordia.core.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SuccessResponseDTO<T> extends AbstractResponseDTO {
    private final T data;

    public SuccessResponseDTO(T data) {
        super(ResponseDTOInterface.SUCCESS);
        this.data = data;
    }
}
