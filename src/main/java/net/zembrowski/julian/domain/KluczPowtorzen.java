package net.zembrowski.julian.domain;



import java.io.Serializable;

public class KluczPowtorzen implements Serializable {

    private String nazwa;
   private int numer;

    public KluczPowtorzen(String nazwa, int numer) {
        this.nazwa = nazwa;
        this.numer = numer;
    }

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
}
