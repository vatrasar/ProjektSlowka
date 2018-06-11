package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

        model.addAttribute("odpowiedz",false);
        model.addAttribute("pyt",wykonywanePytania.get(0));
        return "pytanie";
    }


    @RequestMapping(value = "/robPowtorzenie",method = RequestMethod.POST)
    public String robPowtorzenieform(Pytanie odpowiedz, Model model)
    {
        //pole pytanie w odpowiedzi zawiera teraz odpowiedz uzytkownika
        model.addAttribute("pytanie",odpowiedz);
        return "odpowiedz";
    }
    @RequestMapping(value = "/robPowtorzeniePodsumowanie")
    public String robPowtorzeniePodsumowanie(@RequestParam("id")Integer id,@RequestParam("zal") Integer zal, Model model)
    {
        Status status;
        if (zal==0)
        {
            status=Status.NIEUMIEM
        }
        else
        {
            status=Status.UMIEM;
        }

        pytania.zmienStatusPytania(id,status)
        return "odpowiedz";
    }
}
