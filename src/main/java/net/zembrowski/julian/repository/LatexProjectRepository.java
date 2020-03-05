package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.LatexProject;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Scope("session")
public class LatexProjectRepository {

    @PersistenceContext
    private EntityManager em;
    @Transactional
    public void addNewProject(LatexProject newLatexProject) {
        em.persist(newLatexProject);
    }
}
