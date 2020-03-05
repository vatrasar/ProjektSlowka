package net.zembrowski.julian.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class represents propersties for generation Latex file
 */
@Entity
public class LatexProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String projectName,userName;

    @ElementCollection()
    @CollectionTable(name = "projectChapters")
    private List<String> chaptersNames;//; is separator

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LatexProject() {
//        projectName="";
//        chaptersNames=new ArrayList<>();
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

    public List<String> getChaptersNames() {
        return chaptersNames;
    }

    public void setChaptersNames(List<String> chaptersNames) {
        this.chaptersNames = chaptersNames;
    }
}
