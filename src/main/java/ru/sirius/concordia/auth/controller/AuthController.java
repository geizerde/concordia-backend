package ru.sirius.concordia.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sirius.concordia.auth.model.dto.request.AuthRequestDTO;
import ru.sirius.concordia.auth.model.dto.response.AuthResponseDTO;
import ru.sirius.concordia.auth.service.AuthService;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTOInterface> login(
            @RequestBody @Validated AuthRequestDTO request
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<AuthResponseDTO>builder()
                            .data(
                                    authService.attemptLogin(
                                            request.getEmail(),
                                            request.getPassword()
                                    )
                            )
                            .build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
