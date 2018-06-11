package net.zembrowski.julian.repository;


import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class PowotrzenieRepository {

    @Autowired
    EntityManager em;


    public boolean isExist(Powtorzenie nowePowtorzenie)
    {


        Klucz szukany=new Klucz(nowePowtorzenie.getNumer(),nowePowtorzenie.getNazwa());
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
}
