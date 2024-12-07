package ru.sirius.concordia.core.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.Builder.Default;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SuccessResponseDTO<T> extends AbstractResponseDTO {
    @Default
    protected final boolean status =
            ResponseDTOInterface.SUCCESS;

    protected final T data;
}
