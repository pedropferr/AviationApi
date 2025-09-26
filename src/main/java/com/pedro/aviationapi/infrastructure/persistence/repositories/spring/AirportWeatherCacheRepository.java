package com.pedro.aviationapi.infrastructure.persistence.repositories.spring;

import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportWeatherCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AirportWeatherCacheRepository extends JpaRepository<AirportWeatherCacheEntity, String> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AirportCacheEntity c WHERE c.expiresAt < CURRENT_TIMESTAMP")
    void deleteAllExpired();

}
