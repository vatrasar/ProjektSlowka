package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.RepositoryUzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UzytkownikService {

    @Autowired
  private  RepositoryUzytkownik uzytkownicy;
   public void updateAktualnyUzytkownik()
    {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        uzytkownicy.setActualUser(new Uzytkownik(user.getUsername()," ",true));
    }
 public String getActualUserLogin()
    {
        return uzytkownicy.getActualUser().getLogin();
    }
    public void addUzytkownik(Uzytkownik nowyUzytkownik)
    {
        uzytkownicy.createUzytkonik(nowyUzytkownik);
    }

    public boolean isExist(Uzytkownik szukany) {
       return uzytkownicy.isExist(szukany);
    }
}
