package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PowtarzanieControler {

    @Autowired
    UzytkownikService users;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;


   private static Pytanie aktualnePytanie;
    private static List<Pytanie>actualRepete;

    @RequestMapping(value = "/pokarzPowtorzenia")
    public String pokarzPowtorzenia(Model model)
    {
        users.updateAktualnyUzytkownik();

        model.addAttribute("isTraining",false);
        List<Powtorzenie>powtorzeniaNaDzis=powtorzenia.getPowtorzeniaNaDzis();
        model.addAttribute("powtorzenia",powtorzeniaNaDzis);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
        return "pokarzPowtorzeniaDzis";
    }

    @RequestMapping(value = "/robPowtorzenie")
    public String robPowtorzenie(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer, Model model)
    {
        model.addAttribute("isTraining",true);
        Powtorzenie wykonywane=powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,users.getActualUserLogin()));
        actualRepete=pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);

        //powtorzenie bylo puste, nic sie nie dzieje
        if (actualRepete.isEmpty())
        {

            model.addAttribute("powtorzono",true);
            //nizej to samo co w pokarz powtorzenia
            List<Powtorzenie>powtorzeniaNaDzis=powtorzenia.getRepetsToLearn();
            model.addAttribute("powtorzenia",powtorzeniaNaDzis);
            model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
            return "pokarzPowtorzeniaDzis";
        }


        Pytanie nowy=new Pytanie();
        Pytanie stare=actualRepete.get(0);
        nowy.setId(stare.getId());
        nowy.setAnswer(stare.getAnswer());
        model.addAttribute("odp",nowy);
        aktualnePytanie.setId(stare.getId());
        aktualnePytanie.setPowtorzenie(stare.getPowtorzenie());
        model.addAttribute("pyt",stare);
        return "pytanie";
    }


    @RequestMapping(value = "/robPowtorzenie",method = RequestMethod.POST)
    public String robPowtorzenieform(Pytanie odpowiedz, Model model)
    {

        //pole pytanie w odpowiedzi zawiera teraz odpowiedz uzytkownika
        model.addAttribute("pytanie",odpowiedz);
        return "odpowiedz";
    }
    @RequestMapping(value = "/robPowtorzeniePodsumowanie")
    public String robPowtorzeniePodsumowanie(@RequestParam("zal") Integer zal, Model model)
    {
        Status status;
        if (zal==0)
        {
            status=Status.NIEUMIEM;
        }
        else
        {
            status=Status.UMIEM;
        }

        pytania.zmienStatusPytania(aktualnePytanie.getId(),status);

        return "redirect:/robPowtorzenie?id="+aktualnePytanie.getPowtorzenie().getNazwa()+"&pk="+aktualnePytanie.getPowtorzenie().getNumer();
    }
}
