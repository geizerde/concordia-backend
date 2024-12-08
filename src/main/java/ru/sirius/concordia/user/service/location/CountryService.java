package ru.sirius.concordia.user.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.user.model.dto.location.CountryDTO;
import ru.sirius.concordia.user.model.location.Country;
import ru.sirius.concordia.user.repository.location.CountryRepositoryInterface;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepositoryInterface countryRepository;

    public Country create(CountryDTO countryDTO) {
        Country country = new Country();
        country.setName(countryDTO.getName());

        return countryRepository.save(country);
    }

    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    public Country findById(Long id) {
        return countryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Country is not found")
        );
    }
}
