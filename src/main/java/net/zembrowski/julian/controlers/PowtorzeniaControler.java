package net.zembrowski.julian.controlers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.PytanieRepository;
import net.zembrowski.julian.repository.RoleRepository;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.RoleServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class PowtorzeniaControler {


    @Autowired
    PowtorzenieServices powtorzenia;

    @Autowired
    Uzytkownik aktualnyUzytkownik;

    @Autowired
    Powtorzenie akutalnePowtorzenie;

    @Autowired
    PytanieServices pytania;

    @RequestMapping( value = "/dodaniePowtorzenia")
   public String dodaniePowtorzenia(Model model)
    {

        model.addAttribute("powtorzenie",new Powtorzenie());
        return "rejestracjaPowtorzenia";
    }
    @RequestMapping( value = "/dodaniePowtorzenia",method = RequestMethod.POST)
    public String przyjeciePowtorzenia(Powtorzenie NowePowtorzenie)
    {


        NowePowtorzenie.setWlasciciel(aktualnyUzytkownik.getLogin());
        LocalDate akutalnaData=LocalDate.now();

        NowePowtorzenie.setDzienPowtorzenia(akutalnaData.plusDays(NowePowtorzenie.getNastepne()));
        if(powtorzenia.isExist(NowePowtorzenie))
        {
            return "redirect:/rejestracjaPowtorzeniaBlad";
        }

        powtorzenia.persistPowtorzenie(NowePowtorzenie);
        powtorzenia.ustawJakoAktualne(NowePowtorzenie);
        System.out.print(akutalnePowtorzenie);
        return "redirect:/dodajPytanie";
    }


    @RequestMapping(value = "/dodajPytanie")
    public String rejestrujPytanie(Model model)
    {

        model.addAttribute("pytanie",new Pytanie());
        model.addAttribute("sukces",false);
        return  "dodaniePytania";
    }

    @RequestMapping(value = "/dodajPytanie",method = RequestMethod.POST)
    public String dodajPytanie(Pytanie nowe)
    {
        nowe.setPowtorzenie(akutalnePowtorzenie);
        pytania.createPytanie(nowe);
        return  "redirect:/dodajPytanieSukces";
    }

    @RequestMapping(value = "/dodajPytanieSukces")
    public String rejestrujPytanieSukces(Model model)
    {
        model.addAttribute("pytanie",new Pytanie());
        model.addAttribute("sukces",true);
        return  "dodaniePytania";
    }

    @RequestMapping( value = "/rejestracjaPowtorzeniaBlad")
    public String dodaniePowtorzeniaBlad(Model model)
    {

        model.addAttribute("powtorzenie",new Powtorzenie());
        return "rejestracjaPowtorzeniaBlad";
    }




}
