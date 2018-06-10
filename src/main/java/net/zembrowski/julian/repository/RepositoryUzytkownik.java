package net.zembrowski.julian.repository;


import net.zembrowski.julian.domain.Uzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
public class RepositoryUzytkownik {

    @Autowired
    EntityManager em;

    @Transactional
    public void createUzytkonik(Uzytkownik uzytkownik)
    {
        em.persist(uzytkownik);
    }

    public boolean isExist(Uzytkownik szukany) {


        if(null==em.find(Uzytkownik.class,szukany.getLogin()))
        {
            return false;
        }
        else
            return true;

    }
}
