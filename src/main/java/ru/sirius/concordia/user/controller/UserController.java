package ru.sirius.concordia.user.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sirius.concordia.auth.model.security.rule.UserAuthenticationToken;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;
import ru.sirius.concordia.user.model.dto.TagDTO;
import ru.sirius.concordia.user.model.dto.UserDTO;
import ru.sirius.concordia.user.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ResponseDTOInterface> create(
            @RequestBody UserDTO userDTO
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<UserDTO>builder()
                            .data(
                                    modelMapper.map(
                                            userService.create(
                                                    userDTO
                                            ),
                                            UserDTO.class
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

    @GetMapping("/tags")
    public ResponseEntity<ResponseDTOInterface> getTagsByUserId(
            Principal principal
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<TagDTO>>builder()
                            .data(
                                    userService.getTagsByUserId(((UserAuthenticationToken) principal)
                                                    .getPrincipal()
                                                    .getUserId()
                                            ).stream()
                                            .map(
                                                    tag -> modelMapper.map(
                                                            tag,
                                                            TagDTO.class
                                                    )
                                            )
                                            .toList()
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

    @GetMapping("/me")
    public ResponseEntity<ResponseDTOInterface> getUserById(
            Principal principal
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<UserDTO>builder()
                            .data(
                                    modelMapper.map(
                                            userService.getUserById(
                                                    ((UserAuthenticationToken) principal)
                                                            .getPrincipal()
                                                            .getUserId()
                                            ),
                                            UserDTO.class
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
