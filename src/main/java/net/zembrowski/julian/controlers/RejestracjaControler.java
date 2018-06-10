package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RejestracjaControler {

    @Autowired
    UzytkownikService uzytkownicy;

    @RequestMapping(value ="/rejestruj",method = RequestMethod.POST)
    public String rejestruj(Uzytkownik nowyUzytkownik)
    {
        uzytkownicy.addUzytkownik(nowyUzytkownik);
        return "rejestracja";
    }

}
