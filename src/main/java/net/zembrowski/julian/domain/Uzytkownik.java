package net.zembrowski.julian.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Component
@Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Id
    private String login;
    private String haslo;
    private boolean enabled;


    public Uzytkownik() {
        login="";
    }

    public Uzytkownik(String login, String haslo, boolean enabled) {
        this.login = login;
        this.haslo = haslo;
        this.enabled = enabled;
    }
}
