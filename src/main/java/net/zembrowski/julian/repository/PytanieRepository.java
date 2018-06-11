package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.Pytanie;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PytanieRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void createPytanie(Pytanie nowe)
    {

        em.persist(nowe);
    }
}
