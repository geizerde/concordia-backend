package ru.sirius.concordia.core.model.dto.response;

public interface ResponseDTOInterface {
    boolean SUCCESS = true;
    boolean FAILURE = false;

    boolean getStatus();
}
