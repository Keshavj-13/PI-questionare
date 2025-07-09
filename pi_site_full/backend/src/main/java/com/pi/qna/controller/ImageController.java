package com.pi.qna.controller;

import com.pi.qna.dto.ImageDto;
import com.pi.qna.entity.Image;
import com.pi.qna.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/questions/{qId}/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    public ImageDto upload(@PathVariable Long qId,
                           @RequestPart("file") MultipartFile file) {

        Image img = imageService.attachImage(qId, file);
        return new ImageDto(img.getId(), img.getUrl());
    }
}
