package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class trainingControler {


    @Autowired
    UzytkownikService users;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;

    private static List<Pytanie>actualQuestionsList;
    private static Pytanie aktualnePytanie;

    @RequestMapping(value = "/training")
   public String training(Model model)
    {

        users.updateAktualnyUzytkownik();
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
        model.addAttribute("isTraining",true);

        List<Powtorzenie>toLearn =powtorzenia.getRepetsToLearn();
        model.addAttribute("powtorzenia",toLearn);

        return "pokarzPowtorzeniaDzis";
    }
    @RequestMapping(value = "/cwicz")
    public String work(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer, Model model)
    {
        model.addAttribute("isTraining",false);
        Powtorzenie wykonywane=powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,users.getActualUserLogin()));
        actualQuestionsList=pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);

        //jesli w powtorzeniu nie ma zadnych pytan to nic sie nie dzieje
        if (actualQuestionsList.isEmpty())
        {

            model.addAttribute("powtorzono",true);
            List<Powtorzenie>toLearn =powtorzenia.getRepetsToLearn();
            //nizej to samo co w pokarz powtorzenia
            model.addAttribute("powtorzenia",toLearn);
            model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
            return "pokarzPowtorzeniaDzis";
        }


        Pytanie nowy=new Pytanie();
        Pytanie stare=actualQuestionsList.get(0);
        nowy.setId(stare.getId());
        nowy.setAnswer(stare.getAnswer());
        model.addAttribute("odp",nowy);
        aktualnePytanie.setId(stare.getId());
        aktualnePytanie.setPowtorzenie(stare.getPowtorzenie());
        model.addAttribute("pyt",stare);
        return "pytanie";
    }



}
