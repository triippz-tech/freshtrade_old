package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.Category;
import com.triippztech.freshtrade.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
}
