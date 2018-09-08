package net.zembrowski.julian.controlers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Status;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.PytanieRepository;
import net.zembrowski.julian.repository.RoleRepository;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.RoleServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@Scope("session")
public class DodawaniePowtorzenControler {


    @Autowired
    PowtorzenieServices powtorzenia;

    @Autowired
    UzytkownikService users;

    @Autowired
    Powtorzenie akutalnePowtorzenie;

    @Autowired
    PytanieServices pytania;

    @RequestMapping( value = "/dodaniePowtorzenia")
   public String dodaniePowtorzenia(Model model)
    {

        Powtorzenie nowe = new Powtorzenie();

        nowe.setUtworzenie(LocalDate.now().minusDays(1));
        nowe.setEmpty(true);
        model.addAttribute("powtorzenie", nowe);

        return "rejestracjaPowtorzenia";
    }
    @RequestMapping( value = "/dodaniePowtorzenia",method = RequestMethod.POST)
    public String przyjeciePowtorzenia(Powtorzenie NowePowtorzenie,Model model)
    {



        users.updateAktualnyUzytkownik();
        NowePowtorzenie.setWlasciciel(users.getActualUserLogin());
        NowePowtorzenie.setNumer(powtorzenia.getMaxNumer(NowePowtorzenie.getNazwa())+1);
        LocalDate akutalnaData=LocalDate.now();


        NowePowtorzenie.setDzien(akutalnaData.plusDays(NowePowtorzenie.getNastepne()));
        if(powtorzenia.isExist(NowePowtorzenie))
        {
            return "redirect:/rejestracjaPowtorzeniaBlad";
        }

        powtorzenia.persistPowtorzenie(NowePowtorzenie);
        powtorzenia.ustawJakoAktualne(NowePowtorzenie);

        if (NowePowtorzenie.isEmpty())
        {
            model.addAttribute("nowePow",NowePowtorzenie);
            return "podsumowanieDodanegoPowtorzenia";
        }
        else
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
    @Transactional
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

    @RequestMapping("/zmianaPytania")
    public String zmienPytanie(@RequestParam("id") int id, Model model)
    {
        Pytanie edytowane=new Pytanie();
        edytowane.setId(id);
        model.addAttribute("pytanie",edytowane);
        model.addAttribute("edition",true);
       return "pytanieEdition";
    }

    @RequestMapping(value = "/zmianaPytania",method = RequestMethod.POST)
    public String edycjaPytanie(Pytanie edytowane)
    {
        pytania.editPytanie(edytowane);
        return "redirect:/training";
    }





}
