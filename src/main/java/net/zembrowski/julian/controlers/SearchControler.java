package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Scope("session")
public class SearchControler {

    @Autowired
    UzytkownikService users;

    @Autowired
    PowtorzenieServices repetitions;
    @Autowired
    TrainingControler trainingControler;

    @RequestMapping("/search")
    public String search(Model model)
    {
        users.updateAktualnyUzytkownik();
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());

        return "searchConditions";
    }

    @PostMapping("/search")
    public String searchResults(Model model, @RequestParam("name")String name)
    {


        List<Powtorzenie>powList=repetitions.getPowtorzeniaByName(name);
        trainingControler.setToLearn(powList);

        return "redirect:/training";
    }
}
