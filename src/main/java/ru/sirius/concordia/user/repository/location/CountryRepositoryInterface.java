package ru.sirius.concordia.user.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.location.Country;

public interface CountryRepositoryInterface extends JpaRepository<Country, Long> {}
