package com.abac.visorbe2.controllers;

import com.abac.visorbe2.dto.RateImageDto;
import com.abac.visorbe2.dto.response.CurrentImageDto;
import com.abac.visorbe2.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/visor")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/image")
    public ResponseEntity<CurrentImageDto> getCurrentImage() {
        var currentImageDto = imageService.getCurrentImageIndex();
        return ResponseEntity.ok(currentImageDto);
    }

    @GetMapping("/imageData")
    public @ResponseBody byte[] getCurrentImageData() {
        return imageService.getCurrentImageData();
    }

    @PostMapping("/image")
    public ResponseEntity<Long> rateCurrentImage(@RequestBody RateImageDto rateImageDto) {
        var id = imageService.handleImageRating(rateImageDto);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file ) {
        long id = imageService.saveNewImage(file);
        return ResponseEntity.ok(id);
    }

}
