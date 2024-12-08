package ru.sirius.concordia.user.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.location.Region;

import java.util.List;

public interface RegionRepositoryInterface extends JpaRepository<Region, Long> {
    List<Region> findAllByCountryId(Long countryId);
}
