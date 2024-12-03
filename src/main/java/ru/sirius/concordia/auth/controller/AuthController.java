package ru.sirius.concordia.auth.controller;

import ru.sirius.concordia.auth.model.dto.request.AuthRequestDTO;
import ru.sirius.concordia.auth.model.dto.entity.UserDTO;
import ru.sirius.concordia.auth.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.auth.service.AuthService;
import ru.sirius.concordia.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Validated AuthRequestDTO request
    ) {
        try {
            return new ResponseEntity<>(
                    authService.attemptLogin(request.getNickname(), request.getPassword()),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder().message(e.getMessage()).build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(
            @RequestBody UserDTO userDTO
    ) {
        try {
            return new ResponseEntity<>(
                    userService.create(
                            userDTO
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder().message(e.getMessage()).build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
