package net.zembrowski.julian.repository;


import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Tag;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Repository
@Scope("session")
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


    /**
     *Function returns repeats from present day and every repeat which wasn't done in before days
     * @param dzien
     * @return
     */
    public List<Powtorzenie> getPowtorzeniaNaDzis(LocalDate dzis) {

        List<Powtorzenie> result=null;
        while (true) {
            try {
                result = em.createQuery("select p from Powtorzenie p WHERE p.dzien<=:dzis AND p.wlasciciel=:akutalny order by nazwa,nastepne", Powtorzenie.class).setParameter("dzis", dzis).setParameter("akutalny", akutalnyUzytkownik.getLogin()).getResultList();
            } catch (Exception e) {
                e.printStackTrace();
            }

            break;
        }
        return result;
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

    /**
     * Function returns repetes  only from one Day (repeats from before days are ignore)
     * @param dzien
     * @return
     */
    @Transactional
    public List<Powtorzenie> getPowtorzeniaNaDzien(LocalDate dzien) {

        return em.createQuery("select p from Powtorzenie p WHERE p.dzien=:szukany AND p.wlasciciel=:akutalny order by nazwa",Powtorzenie.class).setParameter("szukany",dzien).setParameter("akutalny",akutalnyUzytkownik.getLogin()).getResultList();

    }


    public List<Powtorzenie> getPowtorzeniaByName(String name) {
        users.updateAktualnyUzytkownik();

        return em.createQuery("SELECT p from Powtorzenie p where nazwa=:wantedName and wlasciciel=:user",Powtorzenie.class).setParameter("wantedName",name).setParameter("user",users.getActualUserLogin()).getResultList();
    }

    public List<Powtorzenie> getPowtorzeniaByDate(LocalDate repetitionDate) {

        return em.createQuery("SELECT p from Powtorzenie p where utworzenie=:wantedDate and wlasciciel=:user",Powtorzenie.class).setParameter("wantedDate",repetitionDate).setParameter("user",users.getActualUserLogin()).getResultList();

    }

    public Powtorzenie getTagPow(Tag tag) {

        users.updateAktualnyUzytkownik();
        if(tag.getPowtorzenie()==null)
         return em.createQuery("SELECT p from Powtorzenie p join Pytanie py ON p=py.powtorzenie where py.id=:tagP and p.wlasciciel=:user",Powtorzenie.class).setParameter("tagP",tag.getPytanie().getId()).setParameter("user",users.getActualUserLogin()).getSingleResult();
        else
            return em.find(Powtorzenie.class,tag.getPowtorzenie());

    }
}
