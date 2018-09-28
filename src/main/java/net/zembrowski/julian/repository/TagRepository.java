package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagRepository {
    @PersistenceContext
   private EntityManager em;


    public void createTag(Tag tag) {
        em.persist(tag);
    }

    public void dropTag(Tag id) {

        em.remove(id);
    }

    public List<Tag> getTagsOfPytanie(Pytanie pytanie) {

        return em.createQuery("SELECT t FROM Tag t where t.pytanie=:processingQuestion",Tag.class).setParameter("processingQuestion",pytanie).getResultList();

    }
}
