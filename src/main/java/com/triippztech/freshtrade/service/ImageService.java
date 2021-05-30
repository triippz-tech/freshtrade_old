package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.Image;
import com.triippztech.freshtrade.repository.ImageRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Image}.
 */
@Service
@Transactional
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Save a image.
     *
     * @param image the entity to save.
     * @return the persisted entity.
     */
    public Image save(Image image) {
        log.debug("Request to save Image : {}", image);
        return imageRepository.save(image);
    }

    /**
     * Partially update a image.
     *
     * @param image the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Image> partialUpdate(Image image) {
        log.debug("Request to partially update Image : {}", image);

        return imageRepository
            .findById(image.getId())
            .map(
                existingImage -> {
                    if (image.getImageUrl() != null) {
                        existingImage.setImageUrl(image.getImageUrl());
                    }
                    if (image.getCreatedDate() != null) {
                        existingImage.setCreatedDate(image.getCreatedDate());
                    }
                    if (image.getIsVisible() != null) {
                        existingImage.setIsVisible(image.getIsVisible());
                    }

                    return existingImage;
                }
            )
            .map(imageRepository::save);
    }

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Image> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        return imageRepository.findAll(pageable);
    }

    /**
     * Get one image by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Image> findOne(UUID id) {
        log.debug("Request to get Image : {}", id);
        return imageRepository.findById(id);
    }

    /**
     * Delete the image by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Image : {}", id);
        imageRepository.deleteById(id);
    }
}
