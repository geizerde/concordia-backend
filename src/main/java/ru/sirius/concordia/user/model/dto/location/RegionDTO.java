package ru.sirius.concordia.user.model.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionDTO {
    private Long id;

    private String name;

    private Long countryId;
}
