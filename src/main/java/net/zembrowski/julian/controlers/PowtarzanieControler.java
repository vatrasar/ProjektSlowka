package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.PowtorzenieServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PowtarzanieControler {

    @Autowired
    Uzytkownik akutalnyUzytkownik;
    @Autowired
    PowtorzenieServices powtorzenia;
    @RequestMapping(value = "/pokarzPowtorzenia")
    public String pokarzPowtorzenia(Model model)
    {
        List<Powtorzenie>powtorzeniaNaDzis=powtorzenia.getPowtorzeniaNaDzis();
        model.addAttribute("powtorzenia",powtorzeniaNaDzis);
        return "pokarzPowtorzenia";
    }
}
