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
import java.util.List;

@Repository
@Scope("session")
public class LatexProjectRepository {

    @PersistenceContext
    private EntityManager em;
    @Transactional
    public void addNewProject(LatexProject newLatexProject) {
        em.persist(newLatexProject);
    }
    @Transactional
    public List<LatexProject> getLatexProjectList(String actualUserLogin) {
        return em.createQuery("SELECT l FROM LatexProject l WHERE l.userName=:actualUser",LatexProject.class).setParameter("actualUser",actualUserLogin).getResultList();
    }

    public LatexProject getLatexProject(int projectId) {
        return em.find(LatexProject.class,projectId);
    }
    @Transactional
    public void updateProject(LatexProject latexProject) {
        em.merge(latexProject);
    }
    @Transactional
    public void dropProject(Integer projectId) {
        em.remove(getLatexProject(projectId));
    }
}
