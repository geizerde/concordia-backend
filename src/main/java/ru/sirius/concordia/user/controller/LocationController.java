package ru.sirius.concordia.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;
import ru.sirius.concordia.user.model.location.City;
import ru.sirius.concordia.user.model.location.Country;
import ru.sirius.concordia.user.model.location.Region;
import ru.sirius.concordia.user.service.location.CityService;
import ru.sirius.concordia.user.service.location.CountryService;
import ru.sirius.concordia.user.service.location.RegionService;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@AllArgsConstructor
public class LocationController {
    private final CountryService countryService;

    private final RegionService regionService;

    private final CityService cityService;

    @GetMapping("/countries")
    public ResponseEntity<ResponseDTOInterface> getAllCountries() {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<Country>>builder()
                            .data(
                                    countryService.findAll()
                            )
                            .build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }

    @GetMapping("/regions/{countryId}")
    public ResponseEntity<ResponseDTOInterface> getRegionsByCountry(
            @PathVariable Long countryId
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<Region>>builder()
                            .data(
                                    regionService.findAllByCountryId(countryId)
                            )
                            .build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }

    @GetMapping("/cities/{regionId}")
    public ResponseEntity<ResponseDTOInterface> getCitiesByRegion(
            @PathVariable Long regionId
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<City>>builder()
                            .data(
                                    cityService.findAllByRegionId(regionId)
                            )
                            .build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
