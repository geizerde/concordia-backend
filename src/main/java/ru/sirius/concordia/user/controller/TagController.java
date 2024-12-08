package ru.sirius.concordia.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sirius.concordia.auth.model.security.rule.UserAuthenticationToken;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;
import ru.sirius.concordia.user.model.Tag;
import ru.sirius.concordia.user.model.User;
import ru.sirius.concordia.user.model.dto.request.UserTagsRequestDTO;
import ru.sirius.concordia.user.service.TagService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ResponseDTOInterface> getAllTags() {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<Tag>>builder()
                            .data(
                                    tagService.getAllTags()
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

    @PostMapping
    public ResponseEntity<ResponseDTOInterface> createTag(
            @RequestBody String name
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<Tag>builder()
                            .data(
                                    tagService.createTag(name)
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

    @PostMapping("/user")
    public ResponseEntity<ResponseDTOInterface> addTagsToUser(
            @RequestBody UserTagsRequestDTO request,
            Principal principal
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<User>builder()
                            .data(
                                    tagService.addTagsToUser(
                                            ((UserAuthenticationToken) principal)
                                                    .getPrincipal()
                                                    .getUserId(),
                                            request.getTagIds()
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

    @GetMapping("/{tagId}/users")
    public ResponseEntity<ResponseDTOInterface> getUsersByTagId(
            @PathVariable Long tagId
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<User>>builder()
                            .data(
                                    tagService.getUsersByTagId(tagId)
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

