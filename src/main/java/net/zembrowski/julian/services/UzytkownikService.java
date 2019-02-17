package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.RepositoryUzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Scope("session")
public class UzytkownikService {

    @Autowired
    private  RepositoryUzytkownik uzytkownicy;
    @Autowired
    BCryptPasswordEncoder encoder;

    /**
     *
     * @return true when user changed and false if not
     */
   public boolean updateAktualnyUzytkownik()
    {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Uzytkownik old=uzytkownicy.getActualUser();
        if(old.getLogin().equals(""))
        {
            uzytkownicy.setActualUser(new Uzytkownik(user.getUsername()," ",true));
            return true;
        }
        else
        {
            uzytkownicy.setActualUser(new Uzytkownik(user.getUsername()," ",true));
            return false;
        }


    }
    public String getActualUserLogin()
    {
        return uzytkownicy.getActualUser().getLogin();
    }
    public void addUzytkownik(Uzytkownik nowyUzytkownik)
    {
        nowyUzytkownik.setHaslo(encoder.encode(nowyUzytkownik.getHaslo()));
        uzytkownicy.createUzytkonik(nowyUzytkownik);
    }

    public boolean isExist(Uzytkownik szukany) {
       return uzytkownicy.isExist(szukany);
    }
}
