package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Uzytkownik {

    @Id
    String login;
    String haslo;

    public Uzytkownik() {
    }


}
