package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Uzytkownik {

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    @Id

    String login;
    String haslo;

    public Uzytkownik() {
    }


}
