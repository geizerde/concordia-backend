package ru.sirius.concordia.match.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchesForUserRequestDTO {
    @Builder.Default
    @JsonProperty("count_neighbors")
    private Long countNeighbors = 10L;

    @Builder.Default
    @JsonProperty("mutation_chance")
    private Double mutationChance = 0.1;
}
