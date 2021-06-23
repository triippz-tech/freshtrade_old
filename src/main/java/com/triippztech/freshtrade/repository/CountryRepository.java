package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.Country;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long>, JpaSpecificationExecutor<Country> {
    @Query("SELECT c FROM Country c")
    List<Country> findAllNoPage();
}
