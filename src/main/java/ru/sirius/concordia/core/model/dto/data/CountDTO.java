package ru.sirius.concordia.core.model.dto.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountDTO {
    private final int count;
}
