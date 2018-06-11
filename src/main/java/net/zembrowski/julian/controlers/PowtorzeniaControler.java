package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.RoleRepository;
import net.zembrowski.julian.services.PowtorzenieServices;
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
        return "redirect:/pokarzMenu";
    }

    @RequestMapping( value = "/rejestracjaPowtorzeniaBlad")
    public String dodaniePowtorzeniaBlad(Model model)
    {

        model.addAttribute("powtorzenie",new Powtorzenie());
        return "rejestracjaPowtorzeniaBlad";
    }


}
