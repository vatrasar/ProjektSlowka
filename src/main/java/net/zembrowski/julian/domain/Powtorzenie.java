package net.zembrowski.julian.domain;

import javax.persistence.*;

@Entity
@IdClass(Klucz.class)
public class Powtorzenie {

    @Id
    private String nazwa;
    @Id
    private int numer;
    //uzytkownik
    private String wlasciciel;

    //private LocalDateTime utworzenie;

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

    public String getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(String wlasciciel) {
        this.wlasciciel = wlasciciel;
    }
}
