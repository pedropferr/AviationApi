package com.pedro.aviationapi.infrastructure.persistence.adapters;

import com.pedro.aviationapi.application.ports.AirportCachePort;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
import com.pedro.aviationapi.infrastructure.persistence.repositories.spring.AirportCacheRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AirportCacheRepositoryAdapter implements AirportCachePort {

    private final AirportCacheRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public AirportCacheRepositoryAdapter(AirportCacheRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AirportCacheEntity> findByCode(String code) {
        return repository.findByCodeHQL(code);
    }

    @Override
    public Optional<AirportCacheEntity> findByFaaCodeOrIcaoCode(String code, String code2) {
        return repository.findByFaaCodeOrIcaoCode(code, code2);
    }

    @Override
    public void save(AirportCacheEntity airport) {
        repository.save(airport);
    }

    @Override
    @Transactional
    public void deleteAllExpired() {
        repository.deleteAllExpired();
    }

    //Criteria API
    @Override
    public Optional<AirportCacheEntity> findByCodeCriteria(String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AirportCacheEntity> query = cb.createQuery(AirportCacheEntity.class);
        Root<AirportCacheEntity> root = query.from(AirportCacheEntity.class);

        query.select(root).where(
                cb.or(
                        cb.equal(root.get("faaCode"), code),
                        cb.equal(root.get("icaoCode"), code)
                )
        );

        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    @Override
    public List<AirportCacheEntity> findByExpiresAtBefore(LocalDateTime dateTime){
        return repository.findByExpiresAtBefore(dateTime);
    };

    @Override
    public void deleteAll(List<AirportCacheEntity> airportCacheEntities) {
        repository.deleteAll(airportCacheEntities);
    }

    @Override
    public Optional<AirportCacheEntity> findWithPlanesByFaaCodeOrIcaoCode(String code, String code2) {
        return repository.findWithPlanesByFaaCodeOrIcaoCode(code, code2);
    }
}