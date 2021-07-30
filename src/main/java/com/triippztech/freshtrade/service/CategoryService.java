package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.Category;
import com.triippztech.freshtrade.repository.CategoryRepository;
import com.triippztech.freshtrade.repository.ImageRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Category}.
 */
@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final ImageRepository imageRepository;

    public CategoryService(CategoryRepository categoryRepository, ImageRepository imageRepository) {
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    /**
     * Save a category.
     *
     * @param category the entity to save.
     * @return the persisted entity.
     */
    public Category save(Category category) {
        log.debug("Request to save Category : {}", category);
        return categoryRepository.save(category);
    }

    /**
     * Partially update a category.
     *
     * @param category the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Category> partialUpdate(Category category) {
        log.debug("Request to partially update Category : {}", category);

        return categoryRepository
            .findById(category.getId())
            .map(
                existingCategory -> {
                    if (category.getSlug() != null) {
                        existingCategory.setSlug(category.getSlug());
                    }
                    if (category.getName() != null) {
                        existingCategory.setName(category.getName());
                    }
                    if (category.getCreatedDate() != null) {
                        existingCategory.setCreatedDate(category.getCreatedDate());
                    }
                    if (category.getIsActive() != null) {
                        existingCategory.setIsActive(category.getIsActive());
                    }

                    return existingCategory;
                }
            )
            .map(categoryRepository::save);
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll(pageable);
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Category> findOne(UUID id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id);
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
    }

    /**
     * Fina 6 featured categories.
     *
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public List<Category> getFeaturedCategories() {
        log.debug("Request to get 6 featured categories");
        return categoryRepository.findTop6ByIsFeatured(true);
    }

    /**
     * Find a category by its slug.
     *
     * @param slug {@link String} the slug of the category to search
     *
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Category> findBySlug(String slug) {
        log.debug("Request to get Category by Slug: {}", slug);
        return categoryRepository.findBySlugEquals(slug);
    }

    public Category create(Category category) {
        log.debug("Request to save Category : {}", category);
        category.setCreatedDate(ZonedDateTime.now());
        var createdCategory = save(category);
        category
            .getImages()
            .forEach(
                image -> {
                    image.setCategory(createdCategory);
                    image.setCreatedDate(ZonedDateTime.now());
                    image.setIsVisible(true);
                    imageRepository.save(image);
                }
            );
        return createdCategory;
    }

    public Category updateCategory(Category category) {
        log.debug("Request to update Category : {}", category);

        var foundCategory = findOne(category.getId());
        if (foundCategory.isEmpty()) throw new EntityNotFoundException("Category was not found");

        category
            .getImages()
            .forEach(
                image -> {
                    if (
                        image.getId() == null ||
                        foundCategory.get().getImages().stream().noneMatch(image1 -> image1.getImageUrl().equals(image.getImageUrl()))
                    ) {
                        image.setCategory(foundCategory.get());
                        image.setCreatedDate(ZonedDateTime.now());
                        image.setIsVisible(true);
                        imageRepository.save(image);
                    }
                }
            );
        return save(category);
    }
}
