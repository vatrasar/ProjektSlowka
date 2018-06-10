package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(KluczPowtorzen.class)
public class Powtorzenie {

    @Id
    private String nazwa;
    @Id
    private int numer;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getNumer() {
        return numer;
    }

    public void setNumer(int numer) {
        this.numer = numer;
    }

    public Powtorzenie() {
    }
}
