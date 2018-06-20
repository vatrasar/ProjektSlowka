package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Status;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PytanieRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void createPytanie(Pytanie nowe)
    {

        nowe.setStatus(Status.NIESPRAWDZONE);
        em.persist(nowe);
    }

    public List<Pytanie> getPytaniaPowtorzeniaNiesprawdzone(Powtorzenie wykonywane) {

        return em.createQuery("SELECT p from Pytanie p where p.powtorzenie=:obecne AND p.status=:stat",Pytanie.class).setParameter("obecne",wykonywane).setParameter("stat",Status.NIESPRAWDZONE).getResultList();
    }

    public List<Pytanie>getBledy(Powtorzenie wykonywane) {

       return em.createQuery("SELECT p from Pytanie p where p.powtorzenie=:obecne AND p.status=:stat",Pytanie.class).setParameter("obecne",wykonywane).setParameter("stat",Status.NIEUMIEM).getResultList();

    }

    @Transactional
    public void dodajDoPowtorzenia(Pytanie modyfikowane, Powtorzenie powDlaBledow) {

        modyfikowane.setPowtorzenie(powDlaBledow);
        em.merge(modyfikowane);
    }

    @Transactional
    public void nadajStatusNiesprawdzone(Powtorzenie wykonywane) {

        List<Pytanie>listaPytan=em.createQuery("SELECT p from Pytanie p where p.powtorzenie=:obecne",Pytanie.class).setParameter("obecne",wykonywane).getResultList();
        for (Pytanie modyfikowane:listaPytan)
        {
            modyfikowane.setStatus(Status.NIESPRAWDZONE);
            em.merge(modyfikowane);
        }
    }

    @Transactional
    public void zmienStatusPytania(Integer id, Status status) {
        Pytanie pytanie=em.find(Pytanie.class,id);
        pytanie.setStatus(status);
        em.merge(pytanie);
    }
    @Transactional
    public void deletePytanie(int id) {
        Pytanie deleteQuestion=em.find(Pytanie.class,id);
        em.remove(deleteQuestion);
    }

    public Pytanie getPytanie(int id) {
        return em.find(Pytanie.class,id);
    }
}
