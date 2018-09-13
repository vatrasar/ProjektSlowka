package net.zembrowski.julian.repository;

import jdk.internal.instrumentation.Logger;
import net.zembrowski.julian.domain.MediaSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Scope("session")
public class MediaSourceRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void persistanceMediaSource(MediaSource newest)
    {



            em.persist(newest);


    }
}
