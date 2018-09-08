package net.zembrowski.julian.controlers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.repository.PowotrzenieRepository;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Controller
@Scope("session")
public class editionControler {

    @Autowired
    UzytkownikService uytkownicy;
    @Autowired
    Powtorzenie akutalnePowtorzenie;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;

    @RequestMapping(value="/menuEdycji")
    public String menu(@RequestParam(name = "id")String nazwa, @RequestParam(name = "pk")int numer, Model model,@RequestParam(name = "source")String source)
    {

        powtorzenia.ustawJakoAktualne(powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,uytkownicy.getActualUserLogin())));

        if (source.equals("training"))
        {
            if (akutalnePowtorzenie.isEmpty()) {
                model.addAttribute("puste", true);
                Logger.getGlobal().warning(akutalnePowtorzenie.toString());
            }
            else {
                model.addAttribute("puste", false);
                Logger.getGlobal().warning("dodano false");
                Logger.getGlobal().warning(akutalnePowtorzenie.toString());
            }
            uytkownicy.updateAktualnyUzytkownik();
            model.addAttribute("user",uytkownicy.getActualUserLogin());
            uytkownicy.updateAktualnyUzytkownik();
            return "editionMenu";
        }
        else //dotyczy PokarzPowtorzeniaNaDzis(nie bedzie mozna dodawać nowych pytan
        {

            //tu bedzie odrazu edycja pytania
            return "redirect:/pokarzMenu";//atrapa
        }

    }

    @RequestMapping(value = "/pytanieAdd")
    public String rejestrujPytanie(Model model)
    {
        uytkownicy.updateAktualnyUzytkownik();
        model.addAttribute("edition",true);
        model.addAttribute("pytanie",new Pytanie());
        model.addAttribute("sukces",false);
        model.addAttribute("user",uytkownicy.getActualUserLogin());
        return  "addPytanieEdition";
    }

    @RequestMapping(value = "/pytanieAdd",method = RequestMethod.POST)
    @Transactional
    public String dodajPytanie(Pytanie nowe)
    {
        nowe.setPowtorzenie(akutalnePowtorzenie);
        pytania.createPytanie(nowe);
        return  "redirect:/pytanieAdd";
    }
    @RequestMapping("/dropPow")
    public String dropPow()
    {
        pytania.dropPytaniaOfPowtorzenie(akutalnePowtorzenie);
        powtorzenia.dropPowotrzenie(akutalnePowtorzenie);
        return "redirect:/training";
    }

    @RequestMapping("/edit")
    public String edit(Model model)
    {
        model.addAttribute("powtorzenie",akutalnePowtorzenie);
        return "powtorzenieEdition";
    }

    @PostMapping("/edit")
    @Transactional
    public String editPost(Powtorzenie NowePowtorzenie, Model model)
    {

        uytkownicy.updateAktualnyUzytkownik();

        NowePowtorzenie.setWlasciciel(uytkownicy.getActualUserLogin());
        NowePowtorzenie.setDzien(akutalnePowtorzenie.getDzien());


        List<Pytanie>pytaniaStaregoPowtorzenia=pytania.getPytaniaPowtorzenia(akutalnePowtorzenie);

        powtorzenia.dropPowotrzenie(akutalnePowtorzenie);
        //uwaga to musi byc po usunieciu starego powtorzenia!
        NowePowtorzenie.setNumer(powtorzenia.getMaxNumer(NowePowtorzenie.getNazwa())+1);

        if(powtorzenia.isExist(NowePowtorzenie))
        {
            powtorzenia.persistPowtorzenie(akutalnePowtorzenie);
            pytania.addPytaniaToPowtorzenie(akutalnePowtorzenie,pytaniaStaregoPowtorzenia);
            return "/pokarzMenu";
        }

        powtorzenia.persistPowtorzenie(NowePowtorzenie);

        if(!NowePowtorzenie.isEmpty())
        pytania.addPytaniaToPowtorzenie(NowePowtorzenie,pytaniaStaregoPowtorzenia);

        powtorzenia.ustawJakoAktualne(NowePowtorzenie);

        if (NowePowtorzenie.isEmpty())
        {
            model.addAttribute("nowePow",NowePowtorzenie);
            return "podsumowanieDodanegoPowtorzenia";
        }
        else
            return "redirect:/dodajPytanie";

    }



}
