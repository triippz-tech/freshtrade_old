package com.triippztech.freshtrade.web.rest;

import com.triippztech.freshtrade.domain.Category;
import com.triippztech.freshtrade.repository.CategoryRepository;
import com.triippztech.freshtrade.service.CategoryQueryService;
import com.triippztech.freshtrade.service.CategoryService;
import com.triippztech.freshtrade.service.criteria.CategoryCriteria;
import com.triippztech.freshtrade.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.triippztech.freshtrade.domain.Category}.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "category";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    private final CategoryQueryService categoryQueryService;

    public CategoryResource(
        CategoryService categoryService,
        CategoryRepository categoryRepository,
        CategoryQueryService categoryQueryService
    ) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.categoryQueryService = categoryQueryService;
    }

    /**
     * {@code POST  /categories} : Create a new category.
     *
     * @param category the category to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new category, or with status {@code 400 (Bad Request)} if the category has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Category result = categoryService.save(category);
        return ResponseEntity
            .created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categories/:id} : Updates an existing category.
     *
     * @param id the id of the category to save.
     * @param category the category to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated category,
     * or with status {@code 400 (Bad Request)} if the category is not valid,
     * or with status {@code 500 (Internal Server Error)} if the category couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody Category category
    ) throws URISyntaxException {
        log.debug("REST request to update Category : {}, {}", id, category);
        if (category.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, category.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Category result = categoryService.save(category);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, category.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categories/:id} : Partial updates given fields of an existing category, field will ignore if it is null
     *
     * @param id the id of the category to save.
     * @param category the category to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated category,
     * or with status {@code 400 (Bad Request)} if the category is not valid,
     * or with status {@code 404 (Not Found)} if the category is not found,
     * or with status {@code 500 (Internal Server Error)} if the category couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Category> partialUpdateCategory(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody Category category
    ) throws URISyntaxException {
        log.debug("REST request to partial update Category partially : {}, {}", id, category);
        if (category.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, category.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Category> result = categoryService.partialUpdate(category);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, category.getId().toString())
        );
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(CategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Categories by criteria: {}", criteria);
        Page<Category> page = categoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categories} : get featured categories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories/featured")
    public ResponseEntity<List<Category>> getFeaturedCategories() {
        log.debug("REST request to get featured Categories by criteria");
        List<Category> categories = categoryService.getFeaturedCategories();
        return ResponseEntity.ok().body(categories);
    }

    /**
     * {@code GET  /categories/count} : count all the categories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/categories/count")
    public ResponseEntity<Long> countCategories(CategoryCriteria criteria) {
        log.debug("REST request to count Categories by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /categories/:id} : get the "id" category.
     *
     * @param id the id of the category to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the category, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable UUID id) {
        log.debug("REST request to get Category : {}", id);
        Optional<Category> category = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(category);
    }

    /**
     * {@code GET  /categories/:slug} : get the "slug" category.
     *
     * @param slug the slug of the category to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the category, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categories/slug/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String slug) {
        log.debug("REST request to get Category : {}", slug);
        Optional<Category> category = categoryService.findBySlug(slug);
        return ResponseUtil.wrapOrNotFound(category);
    }

    /**
     * {@code DELETE  /categories/:id} : delete the "id" category.
     *
     * @param id the id of the category to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        log.debug("REST request to delete Category : {}", id);
        categoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
