package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Statistics;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Statistics createStatistica(Pytanie question)
    {
        Statistics newStatistic=new Statistics();
        em.persist(newStatistic);
        question.setStatistics(newStatistic);
        em.merge(question);
        return newStatistic;
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
    /**
     * remove also statistics class
     */
    public void deletePytanie(int id) {
        Pytanie deleteQuestion=em.find(Pytanie.class,id);
        Statistics statistics=deleteQuestion.getStatistics();
        if(statistics!=null)
            em.remove(deleteQuestion.getStatistics());
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

        if(pyt.getStatistics()!=null)
            em.merge(pyt.getStatistics());
        em.merge(pyt);
    }

    public List<Pytanie> getOneWayCheckedQuestions(Powtorzenie wykonywane) {

        return em.createQuery("SELECT p from Pytanie p where p.powtorzenie=:obecne AND p.status=:stat",Pytanie.class).setParameter("obecne",wykonywane).setParameter("stat",Status.UMIEM_JEDNA_STRONE).getResultList();
    }

    public Pytanie getLastAdded(Powtorzenie powtorzenie) throws javax.persistence.NoResultException {

        return em.createQuery("SELECT p FROM Pytanie p where p.powtorzenie=:pow AND lastAdded=TRUE",Pytanie.class).setParameter("pow",powtorzenie).getSingleResult();
    }

    /**
     * also check whether question has statistic class and add it if not
     * @param bledy
     * @return
     */
    public List<Pytanie> getHardQuestions(List<Pytanie> bledy) {


        //check whether question has statistic class and add it if not
        for(Pytanie blad :bledy)
        {
            if(blad.getStatistics()==null)
            {
                createStatistica(blad);

            }
        }

        return bledy.stream().filter(Pytanie::isHard).collect(Collectors.toList());
    }


}
