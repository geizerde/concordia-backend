package ru.sirius.concordia.auth.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.sirius.concordia.user.model.Role;
import ru.sirius.concordia.user.model.dto.*;
import ru.sirius.concordia.user.model.dto.location.CityDTO;
import ru.sirius.concordia.user.model.dto.location.CountryDTO;
import ru.sirius.concordia.user.model.dto.location.RegionDTO;
import ru.sirius.concordia.user.model.location.Country;
import ru.sirius.concordia.user.model.location.Region;
import ru.sirius.concordia.user.service.*;
import ru.sirius.concordia.user.service.location.CityService;
import ru.sirius.concordia.user.service.location.CountryService;
import ru.sirius.concordia.user.service.location.RegionService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final CountryService countryService;
    private final RegionService regionService;
    private final CityService cityService;

    private final List<RoleDTO> defaultRoles = List.of(
            new RoleDTO(null, "User Role", Role.Code.ROLE_USER),
            new RoleDTO(null, "Admin Role", Role.Code.ROLE_ADMIN)
    );

    private final List<CountryDTO> defaultCountries = List.of(
            new CountryDTO(null, "Russia"),
            new CountryDTO(null, "United States")
    );

    private final List<RegionDTO> defaultRegions = List.of(
            new RegionDTO(null, "Moscow Region", null),
            new RegionDTO(null, "California", null)
    );

    private final List<CityDTO> defaultCities = List.of(
            new CityDTO(null, "Moscow", null),
            new CityDTO(null, "Los Angeles", null)
    );

    @Override
    public void run(ApplicationArguments args) {
        createDefaultRoles();
        createDefaultLocations();
        createAdminUser();
    }

    private void createDefaultRoles() {
        for (var role : defaultRoles) {
            roleService.create(role);
        }
    }

    private void createDefaultLocations() {
        defaultCountries.forEach(country -> {
            Country savedCountry = countryService.create(country);

            defaultRegions.stream()
                    .filter(region -> region.getName().equals("Moscow Region") && country.getName().equals("Russia") ||
                            region.getName().equals("California") && country.getName().equals("United States"))
                    .forEach(region -> {
                        region.setCountryId(savedCountry.getId());
                        Region savedRegion = regionService.create(region);

                        defaultCities.stream()
                                .filter(city -> city.getName().equals("Moscow") && region.getName().equals("Moscow Region") ||
                                        city.getName().equals("Los Angeles") && region.getName().equals("California"))
                                .forEach(city -> {
                                    city.setRegionId(savedRegion.getId());
                                    cityService.create(city);
                                });
                    });
        });
    }

    private void createAdminUser() {
        userService.create(
                UserDTO.builder()
                        .password("123")
                        .name("Administrator")
                        .phone("+1234567890")
                        .email("admin@example.com")
                        .age(30)
                        .cityId(1L)
                        .build(),
                Role.Code.ROLE_ADMIN
        );
    }
}

