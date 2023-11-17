package com.abac.visorbe2.services;

import com.abac.visorbe2.dto.RateImageDto;
import com.abac.visorbe2.dto.response.CurrentImageDto;
import com.abac.visorbe2.entities.Image;
import com.abac.visorbe2.entities.ImageLedger;
import com.abac.visorbe2.enums.Rating;
import com.abac.visorbe2.repositories.ImageLedgerRepository;
import com.abac.visorbe2.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImageLedgerRepository imageLedgerRepository;
    private final ImageRepository imageRepository;

    public CurrentImageDto getCurrentImageIndex() {
        return new CurrentImageDto(getCurrentImage().getIMage().getId());
    }

    public byte[] getCurrentImageData() {
        return getCurrentImage().getIMage().getData();
    }

    public Long handleImageRating(RateImageDto rateImageDto) {
        ImageLedger currentImage = getCurrentImage();
        updateCurrentImage(rateImageDto, currentImage);
        return insertNextImageLedger(currentImage);
    }

    public long saveNewImage(MultipartFile file) {
        try {
            Image image = new Image(null,file.getName(),0L, file.getBytes());
            return imageRepository.save(image).getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Long insertNextImageLedger(ImageLedger currentImage) {
        Image nextImage = imageRepository.findById(currentImage.getIMage().getId() + 1)
                .orElseGet(() -> imageRepository.findById(1L).get());
        ImageLedger nextImageLedger = new ImageLedger(null, nextImage, null);
        return imageLedgerRepository.save(nextImageLedger).getId();
    }

    private static void updateCurrentImage(RateImageDto rateImageDto, ImageLedger currentImage) {
        currentImage.setRating(rateImageDto.rating());
        currentImage.getIMage().setScore(rateImageDto.rating() == Rating.HOT ?
                currentImage.getIMage().getScore() + 1 : currentImage.getIMage().getScore() - 1 );
    }

    private ImageLedger getCurrentImage() {
        return imageLedgerRepository.findTopByOrderByIdDesc()
                .orElseGet(initializeFirstImageLedger());
    }

    private Supplier<ImageLedger> initializeFirstImageLedger() {
        return () -> {
            Optional<Image> byId = imageRepository.findById(1L);
            imageLedgerRepository.save(new ImageLedger(null, byId.get(), null));
            return imageLedgerRepository.findById(1L).get();
        };
    }

}
