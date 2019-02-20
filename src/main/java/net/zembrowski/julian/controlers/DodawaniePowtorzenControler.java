package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;

import net.zembrowski.julian.services.PowtorzenieServices;

import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.TagService;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
    @Autowired
    TagService tagService;

    @RequestMapping( value = "/dodaniePowtorzenia")
   public String dodaniePowtorzenia(Model model)
    {

        Powtorzenie nowe = new Powtorzenie();

        nowe.setUtworzenie(LocalDate.now());
        nowe.setEmpty(true);
        nowe.setReverse(false);
        model.addAttribute("powtorzenie", nowe);

        return "rejestracjaPowtorzenia";
    }
    @RequestMapping( value = "/dodaniePowtorzenia",method = RequestMethod.POST)
    public String przyjeciePowtorzenia(Powtorzenie NowePowtorzenie,Model model,@RequestParam("tagi")String tags)
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

        tagService.addRepetitionTags(tags,NowePowtorzenie);

        if (NowePowtorzenie.isEmpty())
        {
            model.addAttribute("nowePow",NowePowtorzenie);
            return "podsumowanieDodanegoPowtorzenia";
        }
        else
        return "redirect:/pytanieAdd";
    }


    @RequestMapping( value = "/rejestracjaPowtorzeniaBlad")
    public String dodaniePowtorzeniaBlad(Model model)
    {

        model.addAttribute("powtorzenie",new Powtorzenie());
        return "rejestracjaPowtorzeniaBlad";
    }

}
