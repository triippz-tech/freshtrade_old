package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    List<Category> findTop6ByIsFeatured(Boolean isFeatured);
    Optional<Category> findBySlugEquals(String slug);
}
