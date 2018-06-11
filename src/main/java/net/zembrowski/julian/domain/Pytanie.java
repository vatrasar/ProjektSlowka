package net.zembrowski.julian.domain;


import javax.persistence.*;

@Entity
public class Pytanie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String pytanie;
    String odpowiedz;
    @OneToOne
    Powtorzenie powtorzenie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPytanie() {
        return pytanie;
    }

    public void setPytanie(String pytanie) {
        this.pytanie = pytanie;
    }

    public String getOdpowiedz() {
        return odpowiedz;
    }

    public void setOdpowiedz(String odpowiedz) {
        this.odpowiedz = odpowiedz;
    }

    public Powtorzenie getPowtorzenie() {
        return powtorzenie;
    }

    public void setPowtorzenie(Powtorzenie powtorzenie) {
        this.powtorzenie = powtorzenie;
    }
}
