package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Status;
import org.springframework.context.annotation.Scope;
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
@Scope("session")
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


    public List<Pytanie> getPytaniaOfPowtorzenie(Powtorzenie powtorzenie) {

        return em.createQuery("select p from Pytanie as p where powtorzenie=:pow",Pytanie.class).setParameter("pow",powtorzenie).getResultList();
    }

    @Transactional
    public void upadatePytanie(Pytanie pyt) {

        em.merge(pyt);
    }

    public List<Pytanie> getOneWayCheckedQuestions(Powtorzenie wykonywane) {

        return em.createQuery("SELECT p from Pytanie p where p.powtorzenie=:obecne AND p.status=:stat",Pytanie.class).setParameter("obecne",wykonywane).setParameter("stat",Status.UMIEM_JEDNA_STRONE).getResultList();
    }

    public Pytanie getLastAdded(Powtorzenie powtorzenie) throws javax.persistence.NoResultException {

        return em.createQuery("SELECT p FROM Pytanie p where p.powtorzenie=:pow AND lastAdded=TRUE",Pytanie.class).setParameter("pow",powtorzenie).getSingleResult();
    }
}
