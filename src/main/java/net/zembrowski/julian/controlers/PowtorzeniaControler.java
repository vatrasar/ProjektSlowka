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

import java.time.LocalDateTime;

@Controller
public class PowtorzeniaControler {


    @Autowired
    PowtorzenieServices powtorzenia;

    @Autowired
    Uzytkownik aktualnyUzytkownik;

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


        if(powtorzenia.isExist(NowePowtorzenie))
        {
            return "redirect:/dodaniePowtorzenia";
        }



        powtorzenia.persistPowtorzenie(NowePowtorzenie);
        return "redirect:/pokarzMenu";
    }


}
