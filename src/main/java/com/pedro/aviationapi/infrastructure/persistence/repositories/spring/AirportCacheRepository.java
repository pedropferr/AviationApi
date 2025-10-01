package com.pedro.aviationapi.infrastructure.persistence.repositories.spring;

import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AirportCacheRepository extends JpaRepository<AirportCacheEntity, String> {

    //Method Query
    Optional<AirportCacheEntity> findByFaaCodeOrIcaoCode(String faaCode, String icaoCode);

    //Consulta
    @Query("SELECT a FROM AirportCacheEntity a " +
            "WHERE a.faaCode = :code OR a.icaoCode = :code")
    Optional<AirportCacheEntity> findByCodeHQL(@Param("code") String code);

    //Native Query
    @Query(value = "SELECT * FROM airport_cache a " +
            "WHERE a.faa_code = :code OR a.icao_code = :code",
            nativeQuery = true)
    Optional<AirportCacheEntity> findByCodeNative(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM AirportCacheEntity c WHERE c.expiresAt < CURRENT_TIMESTAMP")
    void deleteAllExpired();

    List<AirportCacheEntity> findByExpiresAtBefore(LocalDateTime dateTime);

    //b) HQL (Hibernate Query Language) com @Query: Utilize a anotação @Query
    // para escrever uma consulta em HQL que realize um JOIN FETCH para carregar os aviões junto com o aeroporto.
    @Query("SELECT a FROM AirportCacheEntity a LEFT JOIN FETCH a.planes " +
            "WHERE a.faaCode = :faaCode OR a.icaoCode = :icaoCode")
    Optional<AirportCacheEntity> findWithPlanesByFaaCodeOrIcaoCode(
            @Param("faaCode") String faaCode,
            @Param("icaoCode") String icaoCode
    );
}
