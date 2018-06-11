package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import java.io.Serializable;

//klucz dla klasy powtorzenie
public class Klucz implements Serializable {


    private int numer;
    private String nazwa;

    public Klucz() {
    }

    public int getNumer() {
        return numer;
    }

    public void setNumer(int numer) {
        this.numer = numer;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Klucz(int numer, String nazwa) {
        this.numer = numer;
        this.nazwa = nazwa;
    }
}
