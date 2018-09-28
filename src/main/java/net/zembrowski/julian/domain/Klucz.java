package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

//klucz dla klasy powtorzenie
public class Klucz implements Serializable {


    private int numer;
    private String nazwa;
    private String wlasciciel;

    public Klucz(int numer, String nazwa, String wlasciciel) {
        this.numer = numer;
        this.nazwa = nazwa;
        this.wlasciciel = wlasciciel;
    }

    public Klucz(Powtorzenie powtorzenie) {
        this.numer = powtorzenie.getNumer();
        this.nazwa = powtorzenie.getNazwa();
        this.wlasciciel = powtorzenie.getWlasciciel();
    }

    public String getWlasciciel() {

        return wlasciciel;
    }

    public void setWlasciciel(String wlasciciel) {
        this.wlasciciel = wlasciciel;
    }

    public Klucz() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Klucz klucz = (Klucz) o;
        return getNumer() == klucz.getNumer() &&
                Objects.equals(getNazwa(), klucz.getNazwa()) &&
                Objects.equals(getWlasciciel(), klucz.getWlasciciel());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNumer(), getNazwa(), getWlasciciel());
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
