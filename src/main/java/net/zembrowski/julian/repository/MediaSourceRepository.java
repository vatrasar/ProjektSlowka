package net.zembrowski.julian.repository;

import jdk.internal.instrumentation.Logger;
import net.zembrowski.julian.domain.MediaSource;
import net.zembrowski.julian.domain.Pytanie;
import org.hibernate.sql.Select;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public List<MediaSource> getMediaForQuestion(Pytanie pytanie) {

        return em.createQuery("SELECT m FROM MediaSource m where m.pytanie=:processingQuestion",MediaSource.class).setParameter("processingQuestion",pytanie).getResultList();
    }

    public int getMaxId() {


        return em.createQuery("SELECT MAX(m.id) from MediaSource m",Integer.class).getSingleResult();
    }

    @Transactional
    public void dropMediaSource(MediaSource toDrop) {

        em.remove(em.contains(toDrop) ? toDrop : em.merge(toDrop));
    }
}
