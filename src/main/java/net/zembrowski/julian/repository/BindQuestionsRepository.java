package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.BindQuestions;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Status;
import net.zembrowski.julian.services.BindQuestiService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope("session")
public class BindQuestionsRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void addBind(BindQuestions bindQuestions)
    {

        em.persist(bindQuestions);
    }


    public void getBind(int id, int id1)
    {

        String query="SELECT b from BindQuestions b where (b.idQuestion1=:id and b.idQuestion2=:id1) OR (b.idQuestion1=:id1 AND b.idQuestion2=:id)";
        List<BindQuestions> list1= em.createQuery(query).setParameter("id",id).setParameter("id1",id1).getResultList();
        for(BindQuestions bind:list1)
        {
            em.remove(bind);
        }

    }


    public void dropBind(int id, int id1) {


    }
}
