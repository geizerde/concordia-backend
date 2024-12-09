package ru.sirius.concordia.user.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sirius.concordia.auth.model.security.rule.UserAuthenticationToken;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;
import ru.sirius.concordia.user.model.dto.PhotoDTO;
import ru.sirius.concordia.user.service.PhotoService;

import java.security.Principal;

@RestController
@RequestMapping("/api/photos")
@AllArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    private final ModelMapper modelMapper;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDTOInterface> uploadPhoto(
            @RequestParam MultipartFile file,
            Principal principal
    ) {
        try {
            var photo = photoService.uploadPhoto(
                    ((UserAuthenticationToken) principal)
                            .getPrincipal()
                            .getUserId(),
                    file
            );

            return ResponseEntity.ok(
                    SuccessResponseDTO.<PhotoDTO>builder()
                            .data(
                                    modelMapper.map(photo, PhotoDTO.class)
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

    @PostMapping("/{photoId}/set-avatar")
    public ResponseEntity<ResponseDTOInterface> setAvatar(
            @PathVariable Long photoId,
            Principal principal
    ) {
        try {
            var avatar = photoService.setAvatar(
                    ((UserAuthenticationToken) principal)
                            .getPrincipal()
                            .getUserId(),
                    photoId
            );

            return ResponseEntity.ok(
                    SuccessResponseDTO.<PhotoDTO>builder()
                            .data(
                                    modelMapper.map(avatar, PhotoDTO.class)
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

