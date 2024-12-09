package ru.sirius.concordia.user.controller;

import lombok.RequiredArgsConstructor;
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
import ru.sirius.concordia.user.model.dto.request.UserTagsRequestDTO;
import ru.sirius.concordia.user.service.TagService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseDTOInterface> getAllTags() {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<TagDTO>>builder()
                            .data(
                                    tagService.getAllTags().stream()
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

    @PostMapping
    public ResponseEntity<ResponseDTOInterface> createTag(
            @RequestBody TagDTO tag
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<TagDTO>builder()
                            .data(
                                    modelMapper.map(
                                            tagService.createTag(
                                                    tag.getName()
                                            ),
                                            TagDTO.class
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

    @PostMapping("/user")
    public ResponseEntity<ResponseDTOInterface> addTagsToUser(
            @RequestBody UserTagsRequestDTO request,
            Principal principal
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<UserDTO>builder()
                            .data(
                                    modelMapper.map(
                                            tagService.addTagsToUser(
                                                    ((UserAuthenticationToken) principal)
                                                            .getPrincipal()
                                                            .getUserId(),
                                                    request.getTagIds()
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

    @GetMapping("/{tagId}/users")
    public ResponseEntity<ResponseDTOInterface> getUsersByTagId(
            @PathVariable Long tagId
    ) {
        try {
            tagService.getUsersByTagId(tagId).stream()
                    .map(
                            user -> modelMapper.map(
                                    user,
                                    UserDTO.class
                            )
                    )
                    .toList();

            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<UserDTO>>builder()
                            .data(
                                    tagService.getUsersByTagId(tagId).stream()
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

