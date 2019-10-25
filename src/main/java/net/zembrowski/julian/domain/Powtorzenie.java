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
@Scope("session")
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
    //ture when you had problems with that repete
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    private boolean problems;

    //when is true you will have to answer for question and then answer for answer (two ways repete)
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    private boolean reverse;
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    private boolean hard;

  public Powtorzenie()
    {

        hard=false;
    }

    public boolean isHard() {
        return hard;
    }

    public void setHard(boolean hard) {
        this.hard = hard;
    }

    public Powtorzenie(String nazwa, String wlasciciel, int numer)
    {

        this.wlasciciel=wlasciciel;
        this.nazwa=nazwa;
        this.numer=numer;
        hard=false;
    }
    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

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

    /*dodaje odpowiednia liczbe dni do numeru.ustala date kiedy ma byc przeprowadzone nastepne powtorzenie*/
    public void refaktoryzujPowtorzenie() {

        if(isHard())
        {
            switch (nastepne) {
                case 1:
                    nastepne = 3;
                    break;
                case 3:
                    nastepne = 6;
                    break;
                case 6:
                    nastepne=10;
                    break;
                case 10:
                    nastepne = 20;
                    break;
                case 20:
                    nastepne = 30;
                    break;
                case 30:
                    nastepne = 90;
                    break;
                case 90:
                    nastepne = 180;
                    break;
                case 180:
                    nastepne = 360;
                    break;
                case 360:
                    nastepne = 360;
                    break;
            }
        }
        else {

            switch (nastepne) {
                case 1:
                    nastepne = 3;
                    break;
                case 3:
                    nastepne = 10;
                    break;
                case 10:
                    nastepne = 30;
                    break;
                case 20:
                    nastepne = 30;
                    break;
                case 30:
                    nastepne = 90;
                    break;
                case 90:
                    nastepne = 180;
                    break;
                case 180:
                    nastepne = 360;
                    break;
                case 360:
                    nastepne = 360;
                    break;
            }
        }
        dzien=LocalDate.now();
        int nextRepete=nastepne+1;
        dzien=dzien.plusDays(nextRepete);
    }

   public void utworzPowDlaBledow(Powtorzenie stare,int nowyNumer,boolean hard)
   {
       this.setNazwa(stare.getNazwa());
       this.setDzien(LocalDate.now().plusDays(1));
       this.setNumer(nowyNumer);
       this.setNazwa(stare.getNazwa());
       this.setWlasciciel(stare.getWlasciciel());
       this.setNastepne(1);
       this.setUtworzenie(stare.getUtworzenie());
       this.setReverse(stare.reverse);
       this.setHard(hard);
   }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Powtorzenie that = (Powtorzenie) o;
        return getNumer() == that.getNumer() &&
                Objects.equals(getNazwa(), that.getNazwa()) &&
                Objects.equals(getWlasciciel(), that.getWlasciciel());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNazwa(), getNumer(), getWlasciciel());
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }



    @Override
    public String toString() {

        return ""+nazwa+" "+numer+" utworzenie:"+utworzenie;

    }
}
