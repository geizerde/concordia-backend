package ru.sirius.concordia.user.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;
import ru.sirius.concordia.user.model.dto.location.CityDTO;
import ru.sirius.concordia.user.model.dto.location.CountryDTO;
import ru.sirius.concordia.user.model.dto.location.RegionDTO;
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

    private final ModelMapper modelMapper;

    @GetMapping("/countries")
    public ResponseEntity<ResponseDTOInterface> getAllCountries() {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<CountryDTO>>builder()
                            .data(
                                    countryService.findAll().stream()
                                            .map(
                                                    country -> modelMapper.map(
                                                            country,
                                                            CountryDTO.class
                                                    )
                                            )
                                            .toList()
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
                    SuccessResponseDTO.<List<RegionDTO>>builder()
                            .data(
                                    regionService.findAllByCountryId(countryId).stream()
                                            .map(
                                                    region -> modelMapper.map(
                                                            region,
                                                            RegionDTO.class
                                                    )
                                            )
                                            .toList()
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
                    SuccessResponseDTO.<List<CityDTO>>builder()
                            .data(
                                    cityService.findAllByRegionId(regionId).stream()
                                            .map(
                                                    city -> modelMapper.map(
                                                            city,
                                                            CityDTO.class
                                                    )
                                            )
                                            .toList()
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
