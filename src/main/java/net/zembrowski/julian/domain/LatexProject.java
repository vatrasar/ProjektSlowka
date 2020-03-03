package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * class represents propersties for generation Latex file
 */
@Entity
public class LatexProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String projectName;
    private String chaptersNames;//; is separator

    public LatexProject() {
        projectName="";
        chaptersNames="";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getChaptersNames() {
        return chaptersNames;
    }

    public void setChaptersNames(String chaptersNames) {
        this.chaptersNames = chaptersNames;
    }
}
