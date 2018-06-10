package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.PowtorzenieServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PowtorzeniaControler {


    @Autowired
    PowtorzenieServices powtorzenia;

    @Autowired
    Uzytkownik aktualnyUzytkownik;

    @RequestMapping( value = "/dodaniePowtorzenia")
    String dodaniePowtorzenia(Model model)
    {

        model.addAttribute("powtorzenie",new Powtorzenie());
        return "rejestracjaPowtorzenia";
    }
    @RequestMapping( value = "/dodaniePowtorzenia",method = RequestMethod.POST)
    String przyjeciePowtorzenia(Powtorzenie NowePowtorzenie)
    {


        return "rejestracjaPowtorzenia";
    }
}
