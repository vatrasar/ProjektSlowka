package net.zembrowski.julian.domain;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@IdClass(Klucz.class)
@Component
public class Powtorzenie {

    @Id
    private String nazwa;
    @Id
    private int numer;
    //uzytkownik
    @Id
    private String wlasciciel;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate utworzenie;
    //data  nastepnego powtorzenia
    private LocalDate dzien;
    //dni do nastepnego powtorzenia
    private int nastepne;
    public int getNastepne() {
        return nastepne;
    }

    public void setNastepne(int nastepne) {
        this.nastepne = nastepne;
    }




    public LocalDate getUtworzenie() {

        return utworzenie;
    }

    public void setUtworzenie(LocalDate utworzenie) {
        this.utworzenie = utworzenie;
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

    public String getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(String wlasciciel) {
        this.wlasciciel = wlasciciel;
    }


    public LocalDate getDzien() {
        return dzien;
    }

    public void setDzien(LocalDate dzien) {
        this.dzien = dzien;
    }

    @Override
    public String toString() {
        return "Powtorzenie{" +
                "nazwa='" + nazwa + '\'' +
                ", numer=" + numer +
                ", wlasciciel='" + wlasciciel + '\'' +
                ", utworzenie=" + utworzenie +
                ", dzien=" + dzien +
                ", nastepne=" + nastepne +
                '}';
    }
}
