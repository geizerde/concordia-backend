package ru.sirius.concordia.user.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.location.City;

import java.util.List;

public interface CityRepositoryInterface extends JpaRepository<City, Long> {
    List<City> findAllByRegionId(Long regionId);
}
