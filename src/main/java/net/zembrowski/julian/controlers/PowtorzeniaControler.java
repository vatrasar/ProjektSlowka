package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.Uzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PowtorzeniaControler {

    @Autowired
    Uzytkownik aktualnyUzytkownik;
    @RequestMapping( value = "/dodaniePowtorzenia")
    String dodaniePowtorzenia(Model model)
    {

        return "rejestracjaPowtorzenia";
    }
}
