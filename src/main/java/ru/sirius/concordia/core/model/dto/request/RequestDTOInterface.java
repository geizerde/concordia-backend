package ru.sirius.concordia.core.model.dto.request;

import ru.sirius.concordia.core.exception.ValidationException;

public interface RequestDTOInterface {
    void validate() throws ValidationException;
}
