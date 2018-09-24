package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.HtmlClassResolver;
import net.zembrowski.julian.domain.Para;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@Scope("session")
public class MenuControler {

    @Autowired
    UzytkownikService users;

    @Autowired
    PowtorzenieServices powtorzenia;

    @RequestMapping(value = "/pokarzMenu")
   public String wyswietlMenu(Model model)
    {

        users.updateAktualnyUzytkownik();
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());


        return "menu";
    }

    /**
     * Plan controler
     * @param model
     * @return
     */
    @RequestMapping("/licznik")
    public String licz(Model model)
    {
        users.updateAktualnyUzytkownik();
        int liczbaDni=7;
        LocalDate dzis=LocalDate.now();
        Para liczby[]=new Para[liczbaDni];



        for (int i=1;i<=liczbaDni;i++) {
            liczby[i-1]=new Para();
            List<Powtorzenie> powtorzeniaNaTydzien = powtorzenia.getPowtorzeniaNaDzien(dzis.plusDays(i));
            liczby[i-1].setLiczba(powtorzeniaNaTydzien.size());
            powtorzeniaNaTydzien.clear();
            liczby[i-1].setNazwa(dzis.plusDays(i).getDayOfWeek().name());
        }
        model.addAttribute("liczbaPow",liczby);
        model.addAttribute("classResolver",HtmlClassResolver.dark);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
        return "pokarzPlan";
    }

}
