package ru.sirius.concordia.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.core.exception.ValidationException;
import ru.sirius.concordia.core.model.dto.request.RequestDTOInterface;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTagsRequestDTO implements RequestDTOInterface {
    private List<Long> tagIds;

    @Override
    public void validate() throws ValidationException {

    }
}