package net.zembrowski.julian.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Entity
public class Tag {

    private String name;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private  int id;
    @ManyToOne
    private  Pytanie pytanie;

    @ManyToOne
    private Powtorzenie powtorzenie;
    public Tag()
    {

    }

    public Tag(String name, Pytanie pytanie) {
        this.name = name;
        this.pytanie = pytanie;
    }

    public Tag(@Max(50) String name, Powtorzenie powtorzenie) {
        this.name = name;
        this.powtorzenie = powtorzenie;
    }

    public Powtorzenie getPowtorzenie() {
        return powtorzenie;
    }

    public void setPowtorzenie(Powtorzenie powtorzenie) {
        this.powtorzenie = powtorzenie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pytanie getPytanie() {
        return pytanie;
    }

    public void setPytanie(Pytanie pytanie) {
        this.pytanie = pytanie;
    }
}
