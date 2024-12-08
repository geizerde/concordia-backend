package ru.sirius.concordia.user.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.user.model.dto.location.CityDTO;
import ru.sirius.concordia.user.model.location.City;
import ru.sirius.concordia.user.repository.location.CityRepositoryInterface;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepositoryInterface cityRepository;

    private final RegionService regionService;

    public City create(CityDTO cityDTO) {
        City city = new City();
        city.setName(cityDTO.getName());
        city.setRegion(
                regionService.findById(
                        cityDTO.getRegionId()
                )
        );

        return cityRepository.save(city);
    }

    public List<City> findAllByRegionId(Long regionId) {
        return cityRepository.findAllByRegionId(regionId);
    }

    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(
                () -> new RuntimeException("City is not found")
        );
    }
}
