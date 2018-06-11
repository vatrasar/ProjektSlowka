package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PowtarzanieControler {

    @Autowired
    Uzytkownik akutalnyUzytkownik;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;

    @RequestMapping(value = "/pokarzPowtorzenia")
    public String pokarzPowtorzenia(Model model)
    {
        List<Powtorzenie>powtorzeniaNaDzis=powtorzenia.getPowtorzeniaNaDzis();
        model.addAttribute("powtorzenia",powtorzeniaNaDzis);
        model.addAttribute("nazwaUzytkownika",akutalnyUzytkownik.getLogin());
        return "pokarzPowtorzeniaDzis";
    }

    @RequestMapping(value = "/robPowtorzenie")
    public String robPowtorzenie(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer, Model model)
    {
        Powtorzenie wykonywane=powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,akutalnyUzytkownik.getLogin()));
        List<Pytanie> wykonywanePytania=pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);

        //spelnione gdy wszystki powtorzenia sa juz wykonane
        if (wykonywanePytania.isEmpty())
        {
            pytania.zatwierdzWykonaniePowtorzenia(wykonywane);
            model.addAttribute("powtorzono",true);
            //nizej to samo co w pokarz powtorzenia
            List<Powtorzenie>powtorzeniaNaDzis=powtorzenia.getPowtorzeniaNaDzis();
            model.addAttribute("powtorzenia",powtorzeniaNaDzis);
            model.addAttribute("nazwaUzytkownika",akutalnyUzytkownik.getLogin());
            return "pokarzPowtorzeniaDzis";
        }

        return "pokarzPowtorzeniaDzis";
    }
}
