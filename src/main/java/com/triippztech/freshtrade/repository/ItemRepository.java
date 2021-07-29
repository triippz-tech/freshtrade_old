package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.User;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Item entity.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, UUID>, JpaSpecificationExecutor<Item> {
    @Query("select item from Item item where item.owner.login = ?#{principal.username}")
    List<Item> findByOwnerIsCurrentUser();

    Page<Item> findAllByOwner(User owner, Pageable pageable);

    @Query(
        value = "select distinct item from Item item left join fetch item.categories left join fetch item.users",
        countQuery = "select count(distinct item) from Item item"
    )
    Page<Item> findAllWithEagerRelationships(Pageable pageable);

    Page<Item> findTop5ByCreatedDateAfterAndIsActiveTrue(Pageable pageable, ZonedDateTime createdDate);

    Page<Item> findAllByIsFeaturedTrueAndIsActiveTrue(Pageable pageable);

    @Query("select distinct item from Item item left join fetch item.categories left join fetch item.users")
    List<Item> findAllWithEagerRelationships();

    @Query(
        "select item from Item item " +
        "left join fetch item.categories " +
        "left join fetch item.users " +
        "left join fetch item.owner " +
        "where item.id =:id"
    )
    Optional<Item> findOneWithEagerRelationships(@Param("id") UUID id);

    @Query(
        value = "SELECT i FROM Item i " +
        " WHERE lower(i.name) LIKE lower(concat('%', ?1,'%'))" +
        " OR lower(i.details) LIKE lower(concat('%', ?1,'%'))" +
        " OR lower(i.tradeEvent.eventName) LIKE lower(concat('%', ?1,'%'))" +
        " AND i.isActive = true"
    )
    Page<Item> search(String query, Pageable pageable);

    @Query(
        value = "SELECT new com.triippztech.freshtrade.service.dto.metrics.TopSellingItemsDTO (it.item, COUNT(it.item)) FROM ItemToken it" +
        " left join it.item" +
        " WHERE it.item.owner = :user" +
        " GROUP BY it.item" +
        " ORDER BY COUNT(it.item) DESC"
    )
    List<com.triippztech.freshtrade.service.dto.metrics.TopSellingItemsDTO> findTopSellingItemsForUser(@Param("user") User user);
}
