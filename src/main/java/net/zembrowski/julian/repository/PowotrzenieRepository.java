package net.zembrowski.julian.repository;


import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PowotrzenieRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    Uzytkownik akutalnyUzytkownik;
    @Autowired
    UzytkownikService users;

    public boolean isExist(Powtorzenie nowePowtorzenie)
    {

        Powtorzenie a=new Powtorzenie();

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

        return em.createQuery("select p from Powtorzenie p WHERE p.dzien<=:dzis AND p.wlasciciel=:akutalny",Powtorzenie.class).setParameter("dzis",dzis).setParameter("akutalny",akutalnyUzytkownik.getLogin()).getResultList();

    }

    public Powtorzenie getPowtorzenie(Klucz klucz) {

        return em.find(Powtorzenie.class,klucz);
    }

    @Transactional
    public int getMaxNumer(String nazwa) {


        users.updateAktualnyUzytkownik();
        if(0==em.createQuery("select count(p.numer) from Powtorzenie p where p.nazwa=:szukana and p.wlasciciel=:aktualny",Long.class).setParameter("szukana",nazwa).setParameter("aktualny",akutalnyUzytkownik.getLogin()).getSingleResult().intValue())
        {
            return 0;
        }
        String a=em.createQuery("select MAX(p.numer) from Powtorzenie p where p.nazwa=:szukana and p.wlasciciel=:aktualny",Integer.class).setParameter("szukana",nazwa).setParameter("aktualny",akutalnyUzytkownik.getLogin()).getSingleResult().toString();
        return Integer.valueOf(a);
    }

    @Transactional
    public void updatePowtorzenie(Powtorzenie wykonywane)
    {
        em.merge(wykonywane);
    }
    @Transactional
    public void remove(Powtorzenie powtorzenie) {

        em.remove(em.contains(powtorzenie) ? powtorzenie : em.merge(powtorzenie));
    }
    @Transactional
    public List<Powtorzenie> getPowtorzeniaNaDzien(LocalDate dzien) {

        return em.createQuery("select p from Powtorzenie p WHERE p.dzien=:szukany AND p.wlasciciel=:akutalny",Powtorzenie.class).setParameter("szukany",dzien).setParameter("akutalny",akutalnyUzytkownik.getLogin()).getResultList();

    }

}
