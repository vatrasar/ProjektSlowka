package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.LatexProject;
import net.zembrowski.julian.repository.LatexProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Scope("session")
public class LatexProjectService {
    @Autowired
    LatexProjectRepository latexProjectRepository;
    @Autowired
    UzytkownikService users;


    public void addNewProject(LatexProject newLatexProject)
    {
        newLatexProject.setUserName(users.getActualUserLogin());
        latexProjectRepository.addNewProject(newLatexProject);
    }
}
