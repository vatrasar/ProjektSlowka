package net.zembrowski.julian.repository;


import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Uzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PowotrzenieRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    Uzytkownik akutalnyUzytkownik;

    public boolean isExist(Powtorzenie nowePowtorzenie)
    {


        Klucz szukany=new Klucz(nowePowtorzenie.getNumer(),nowePowtorzenie.getNazwa(),nowePowtorzenie.getWlasciciel());
        if(em.find(Powtorzenie.class,szukany)==null)
        {
            return false;
        }
        else
            return true;


    }

    @Transactional
    public void persistPowtorzenie(Powtorzenie nowePowtorzenie) {

        em.persist(nowePowtorzenie);
    }

    public List<Powtorzenie> getPowtorzeniaNaDzis(LocalDate dzis) {

        return em.createQuery("select p from Powtorzenie p WHERE p.dzien=:dzis AND p.wlasciciel=:akutalny",Powtorzenie.class).setParameter("dzis",dzis).setParameter("akutalny",akutalnyUzytkownik.getLogin()).getResultList();

    }

    public Powtorzenie getPowtorzenie(Klucz klucz) {

        return em.find(Powtorzenie.class,klucz);
    }

    public int getMaxNumer(String nazwa) {

        return em.createQuery("select MAX(p.numer) from Powtorzenie p where p.nazwa=:szukana and p.wlasciciel=:aktualny",Integer.class).setParameter("szukana",nazwa).setParameter("aktualny",akutalnyUzytkownik.getLogin()).getSingleResult();
    }
}
