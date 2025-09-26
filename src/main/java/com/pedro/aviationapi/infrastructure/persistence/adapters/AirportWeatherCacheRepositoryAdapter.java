package com.pedro.aviationapi.infrastructure.persistence.adapters;

import com.pedro.aviationapi.application.ports.AirportCachePort;
import com.pedro.aviationapi.application.ports.AirportWeatherCachePort;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportWeatherCacheEntity;
import com.pedro.aviationapi.infrastructure.persistence.repositories.spring.AirportCacheRepository;
import com.pedro.aviationapi.infrastructure.persistence.repositories.spring.AirportWeatherCacheRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class AirportWeatherCacheRepositoryAdapter implements AirportWeatherCachePort {

    private final AirportWeatherCacheRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public AirportWeatherCacheRepositoryAdapter(AirportWeatherCacheRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(AirportWeatherCacheEntity airport) {
        repository.save(airport);
    }

    @Override
    @Transactional
    public void deleteAllExpired() {
        repository.deleteAllExpired();
    }

    //Criteria API
    @Override
    public Optional<AirportWeatherCacheEntity> findByCodeCriteria(String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AirportWeatherCacheEntity> query = cb.createQuery(AirportWeatherCacheEntity.class);
        Root<AirportWeatherCacheEntity> root = query.from(AirportWeatherCacheEntity.class);

        query.select(root).where(
                cb.equal(root.get("stationId"), code)
        );

        return entityManager.createQuery(query).getResultStream().findFirst();
    }
}