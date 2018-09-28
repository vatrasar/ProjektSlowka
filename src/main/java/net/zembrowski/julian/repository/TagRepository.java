package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Tag;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;

@Repository
public class TagRepository {
    @PersistenceContext
   private EntityManager em;


    @Transactional
    public void createTag(Tag tag) {
        em.persist(tag);
    }

    @Transactional
    public void dropTag(Tag id) {

        em.remove(id);
    }

    public List<Tag> getTagsOfPytanie(Pytanie pytanie) {

        return em.createQuery("SELECT t FROM Tag t where t.pytanie=:processingQuestion",Tag.class).setParameter("processingQuestion",pytanie).getResultList();

    }

    /**
     * it returns all users tags!
     * @param tagName
     * @return
     */

    public List<Tag> getTagsByName(String tagName) {


        return em.createQuery("SELECT t FROM Tag t where t.name=:nameWanted",Tag.class).setParameter("nameWanted",tagName).getResultList();


    }

    public List<Tag> getRepetitionTags(Powtorzenie akutalnePowtorzenie) {

        return em.createQuery("SELECT t FROM Tag t where t.powtorzenie=:repetitionWanted",Tag.class).setParameter("repetitionWanted",akutalnePowtorzenie).getResultList();



    }
}
