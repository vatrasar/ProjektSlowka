package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.RepositoryUzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UzytkownikService {

    @Autowired
    RepositoryUzytkownik uzytkownicy;

    public void addUzytkownik(Uzytkownik nowyUzytkownik)
    {
        uzytkownicy.createUzytkonik(nowyUzytkownik);
    }

    public boolean isExist(Uzytkownik szukany) {
       return uzytkownicy.isExist(szukany);
    }
}
