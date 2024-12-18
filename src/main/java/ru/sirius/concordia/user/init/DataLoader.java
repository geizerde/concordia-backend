package ru.sirius.concordia.user.init;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.sirius.concordia.user.model.Role;
import ru.sirius.concordia.user.model.dto.RoleDTO;
import ru.sirius.concordia.user.model.dto.UserDTO;
import ru.sirius.concordia.user.model.dto.location.CityDTO;
import ru.sirius.concordia.user.model.dto.location.CountryDTO;
import ru.sirius.concordia.user.model.dto.location.RegionDTO;
import ru.sirius.concordia.user.service.RoleService;
import ru.sirius.concordia.user.service.TagService;
import ru.sirius.concordia.user.service.UserService;
import ru.sirius.concordia.user.service.location.CityService;
import ru.sirius.concordia.user.service.location.CountryService;
import ru.sirius.concordia.user.service.location.RegionService;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final RoleService roleService;

    private final UserService userService;

    private final CountryService countryService;

    private final RegionService regionService;

    private final CityService cityService;

    private final ModelMapper modelMapper;

    private final TagService tagService;

    private final List<RoleDTO> DEFAULT_ROLES = List.of(
            new RoleDTO(null, "User Role", Role.Code.ROLE_USER),
            new RoleDTO(null, "Admin Role", Role.Code.ROLE_ADMIN)
    );

    private static final Map<String, List<String>> RUSSIAN_REGIONS_AND_CITIES = Map.of(
            "Краснодарский край", List.of("Краснодар", "Сочи", "Новороссийск", "Геленджик", "ПГД Сириус"),
            "Московская область", List.of("Москва", "Химки", "Подольск", "Мытищи", "Коломна"),
            "Санкт-Петербург и Ленинградская область", List.of("Санкт-Петербург", "Гатчина", "Выборг", "Кронштадт", "Пушкин"),
            "Новосибирская область", List.of("Новосибирск", "Бердск", "Искитим", "Обь", "Куйбышев"),
            "Свердловская область", List.of("Екатеринбург", "Нижний Тагил", "Каменск-Уральский", "Серов", "Первоуральск")
    );

    private static final List<String> TAG_NAMES = List.of(
            "Спорт", "Музыка", "Книги", "Путешествия", "Фотография", "Танцы", "Рисование", "Игры", "Программирование", "Кулинария",
            "Йога", "Кино", "Театр", "Садоводство", "Рыбалка", "Охота", "Мода", "Мотоциклы", "Велосипеды", "Автомобили",
            "Наука", "Астрономия", "История", "Политика", "Экономика", "Философия", "Психология", "Медицина", "Живопись", "Скульптура",
            "Волонтерство", "Экология", "Эзотерика", "Бизнес", "Маркетинг", "Фитнес", "Саморазвитие", "Ремонт", "Инженерия", "Стартапы",
            "Космос", "Фотошоп", "Робототехника", "Пение", "Аниме", "Комиксы", "Настольные игры", "Шахматы", "Киберспорт", "Мемы"
    );

    @Override
    public void run(ApplicationArguments args) {
        createCitiesWithRegions();
        createTags();
        createDefaultRoles();
        createAdminUser();
    }

    private void createTags() {
        TAG_NAMES.forEach(
                tagService::createTag
        );
    }

    private void createDefaultRoles() {
        DEFAULT_ROLES.forEach(
                roleService::create
        );
    }

    private void createCitiesWithRegions() {
        CountryDTO russia = new CountryDTO(null, "Russia");
        var savedRussia = countryService.create(russia);

        RUSSIAN_REGIONS_AND_CITIES.forEach((regionName, cities) -> {
            RegionDTO region = new RegionDTO(
                    null,
                    regionName,
                    modelMapper.map(savedRussia, CountryDTO.class)
            );

            var savedRegion = regionService.create(region);

            cities.forEach(cityName -> {
                CityDTO city = new CityDTO(
                        null,
                        cityName,
                        modelMapper.map(savedRegion, RegionDTO.class)
                );

                cityService.create(city);
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
                        .city(
                                modelMapper.map(
                                        cityService.findAllByRegionId(1L).getFirst(),
                                        CityDTO.class
                                )
                        )
                        .build(),
                Role.Code.ROLE_ADMIN
        );
    }
}

