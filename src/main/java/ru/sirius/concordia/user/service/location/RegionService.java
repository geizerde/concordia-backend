package ru.sirius.concordia.user.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.user.model.location.Region;
import ru.sirius.concordia.user.model.dto.location.RegionDTO;
import ru.sirius.concordia.user.repository.location.RegionRepositoryInterface;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepositoryInterface regionRepository;

    private final CountryService countryService;

    public Region create(RegionDTO regionDTO) {
        Region region = new Region();
        region.setName(regionDTO.getName());
        region.setCountry(
                countryService.findById(
                        regionDTO.getCountry().getId()
                )
        );

        return regionRepository.save(region);
    }

    public List<Region> findAllByCountryId(Long countryId) {
        return regionRepository.findAllByCountryId(countryId);
    }

    public Region findById(Long id) {
        return regionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Region is not found")
        );
    }
}
