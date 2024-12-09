package ru.sirius.concordia.user.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.user.model.Photo;
import ru.sirius.concordia.user.model.Role;
import ru.sirius.concordia.user.model.dto.location.CityDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    protected Long id;

    protected String name;

    @JsonIgnore
    protected String password;

    protected String email;

    protected String phone;

    protected String description;

    protected Integer age;

    protected Boolean isActive;

    protected Role.Code roleCode;

    protected CityDTO city;

    protected List<PhotoDTO> photos;

    protected List<TagDTO> tags;
}