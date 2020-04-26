package net.zembrowski.julian.dto;

import net.zembrowski.julian.domain.LatexProject;

public class LatexProjectInfo {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatexProjectInfo(LatexProject latexProject) {
        name=latexProject.getProjectName();
        id=latexProject.getId();
    }
}
