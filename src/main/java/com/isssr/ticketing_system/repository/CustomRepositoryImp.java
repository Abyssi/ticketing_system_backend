package com.isssr.ticketing_system.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

//Implementation of custom repository for executing custom queries
@Repository
@Transactional(readOnly = true)
public class CustomRepositoryImp implements CustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List customQuery(String query) {
        javax.persistence.Query customQuery = entityManager.createNativeQuery(query);
        return customQuery.getResultList();
    }
}
