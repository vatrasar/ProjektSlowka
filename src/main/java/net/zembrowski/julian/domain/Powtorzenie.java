package net.zembrowski.julian.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

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
    private boolean empty;
    //ture then you had problems with that repete
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    private boolean problems;
    public int getNastepne() {
        return nastepne;
    }

    public void setNastepne(int nastepne) {
        this.nastepne = nastepne;
    }

    public boolean isProblems() {
        return problems;
    }

    public void setProblems(boolean problems) {
        this.problems = problems;
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
        return ""+nazwa+" "+numer+" utworzenie:"+utworzenie;
    }

    /*dodaje odpowiednia liczbe dni do numeru.ustala date kiedy ma byc przeprowadzone nastepne powtorzenie*/
    public void refaktoryzujPowtorzenie() {

        switch (nastepne)
        {
            case 1:
                nastepne=3;
                break;
            case 3:
                nastepne=10;
                break;
            case 10:
                nastepne=30;
                break;
            case 30:
                nastepne=90;
                break;
            case 180:
                nastepne=360;
                break;
            case 360:
                nastepne=360;
                break;
        }
        dzien=LocalDate.now();
        dzien=dzien.plusDays(nastepne);
    }

   public void utworzPowDlaBledow(Powtorzenie stare,int nowyNumer)
   {
       this.setNazwa(stare.getNazwa());
       this.setDzien(LocalDate.now().plusDays(1));
       this.setNumer(nowyNumer);
       this.setNazwa(stare.getNazwa());
       this.setWlasciciel(stare.getWlasciciel());
       this.setNastepne(1);
       this.setUtworzenie(stare.getUtworzenie());
   }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Powtorzenie that = (Powtorzenie) o;
        return getNumer() == that.getNumer() &&
                getNastepne() == that.getNastepne() &&
                Objects.equals(getNazwa(), that.getNazwa()) &&
                Objects.equals(getWlasciciel(), that.getWlasciciel()) &&
                Objects.equals(getUtworzenie(), that.getUtworzenie()) &&
                Objects.equals(getDzien(), that.getDzien()) &&
        Objects.equals(isEmpty(), that.isEmpty());
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNazwa(), getNumer(), getWlasciciel(), getUtworzenie(), getDzien(), getNastepne());
    }
}
