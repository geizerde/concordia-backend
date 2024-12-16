package ru.sirius.concordia.match.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sirius.concordia.auth.model.security.rule.UserAuthenticationToken;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;
import ru.sirius.concordia.match.model.dto.MatchDTO;
import ru.sirius.concordia.match.model.dto.request.MatchesForUserRequestDTO;
import ru.sirius.concordia.match.service.MatchService;
import ru.sirius.concordia.user.model.dto.UserDTO;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ResponseDTOInterface> createOrUpdateMatch(
            @RequestBody MatchDTO matchDto,
            Principal principal
    ) {
        try {
            matchDto.setSender(
                    UserDTO.builder()
                            .id(
                                    ((UserAuthenticationToken) principal)
                                            .getPrincipal()
                                            .getUserId()
                            ).build()
            );


            return ResponseEntity.ok(
                    SuccessResponseDTO.<MatchDTO>builder()
                            .data(
                                    modelMapper.map(
                                            matchService.createOrUpdateMatch(
                                                    matchDto
                                            ),
                                            MatchDTO.class
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

    @GetMapping("/last/{count}")
    public ResponseEntity<ResponseDTOInterface> createOrUpdateMatch(
            @PathVariable int count,
            Principal principal
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<UserDTO>>builder()
                            .data(
                                    matchService.getLastedMatchesBySenderId(
                                            ((UserAuthenticationToken) principal)
                                                    .getPrincipal()
                                                    .getUserId(),
                                            count
                                    ).stream()
                                            .map(
                                                    user -> modelMapper.map(
                                                            user,
                                                            UserDTO.class
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

    @GetMapping
    public ResponseEntity<ResponseDTOInterface> getMatchesForUser(
            Principal principal,
            @RequestBody MatchesForUserRequestDTO matchesForUserRequestDTO
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<UserDTO>>builder()
                            .data(
                                    matchService.getSimilarUsers(
                                            ((UserAuthenticationToken) principal)
                                                    .getPrincipal()
                                                    .getUserId(),
                                                    matchesForUserRequestDTO.getCountNeighbors(),
                                                    matchesForUserRequestDTO.getMutationChance()
                                            )
                                            .stream()
                                            .map(
                                                    user -> modelMapper.map(
                                                            user,
                                                            UserDTO.class
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
}
