package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Scope("session")
public class PowtarzanieControler {

    @Autowired
    UzytkownikService users;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;

    @Autowired
    Pytanie aktualnePytanie;

    @RequestMapping(value = "/pokarzPowtorzenia")
    public String pokarzPowtorzenia(Model model)
    {
        users.updateAktualnyUzytkownik();

        List<Powtorzenie>powtorzeniaNaDzis=powtorzenia.getPowtorzeniaNaDzis();
        model.addAttribute("powtorzenia",powtorzeniaNaDzis);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
        return "pokarzPowtorzeniaDzis";
    }

    @RequestMapping(value = "/robPowtorzenie")
    public String robPowtorzenie(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer, Model model)
    {

        users.updateAktualnyUzytkownik();


        Powtorzenie wykonywane=powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,users.getActualUserLogin()));
        //jesli powtorzenie zostało oznaczone(rozmyślnie) jako puste
        if (wykonywane.isEmpty())
        {
            aktualnePytanie.setPowtorzenie(wykonywane);
            int sugerowanyNumerNastepnego=powtorzenia.getMaxNumer(wykonywane.getNazwa())+1;
            model.addAttribute("nazwa",wykonywane.toString());
            model.addAttribute("sugerowanyNumer",sugerowanyNumerNastepnego);
            return "emptyRepete";
        }




        List<Pytanie> wykonywanePytania=pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);



        //spelnione gdy nie ma już niesprawdzonych
        if (wykonywanePytania.isEmpty())
        {

            wykonywanePytania=pytania.getOneWayCheckedQuestions(wykonywane);
            //spełnione gdy nie ma juz niesprawdzonych i powtórzonych w jedną strone
            if (wykonywanePytania.isEmpty()) //
            {
                pytania.zatwierdzWykonaniePowtorzenia(wykonywane);
                model.addAttribute("powtorzono", true);
                //nizej to samo co w pokarz powtorzenia
                List<Powtorzenie> powtorzeniaNaDzis = powtorzenia.getPowtorzeniaNaDzis();
                model.addAttribute("powtorzenia", powtorzeniaNaDzis);
                model.addAttribute("nazwaUzytkownika", users.getActualUserLogin());
                return "pokarzPowtorzeniaDzis";
            }
            //pytanie jest ustawiane jako odpowiedz a odpoweidz jako pytanie
            Pytanie odpowiedz=new Pytanie();
            Pytanie pytanie=wykonywanePytania.get(0);
            pytanie.reverse();
            odpowiedz.setId(pytanie.getId());
            odpowiedz.setAnswer(pytanie.getAnswer());
            model.addAttribute("odp",odpowiedz);
            aktualnePytanie.setPytanie(pytanie);
            model.addAttribute("pyt",pytanie);
            return "pytanie";

        }


        //pytanie to pytanie odpowiedz to odpowiedz
        Pytanie odpowiedz=new Pytanie();
        Pytanie pytanie=wykonywanePytania.get(0);
        odpowiedz.setId(pytanie.getId());
        odpowiedz.setAnswer(pytanie.getAnswer());
        model.addAttribute("odp",odpowiedz);
        aktualnePytanie.setPytanie(pytanie);
        model.addAttribute("pyt",pytanie);



        return "pytanie";
    }


    @RequestMapping(value = "/robPowtorzenie",method = RequestMethod.POST)
    public String robPowtorzenieform(Pytanie odpowiedz, Model model)
    {
        model.addAttribute("isTraining",false);
        //pole pytanie w odpowiedzi zawiera teraz odpowiedz uzytkownika
        model.addAttribute("pytanie",odpowiedz);
        return "odpowiedz";
    }
    @RequestMapping(value = "/robPowtorzeniePodsumowanie")
    public String robPowtorzeniePodsumowanie(@RequestParam("zal") Integer zal, Model model)
    {
        users.updateAktualnyUzytkownik();
        if (isNotSameSession())
        {
            return "redirect:/pokarzPowtorzenia";
        }
        Status status=pytania.determineStatus(zal,aktualnePytanie);

        pytania.zmienStatusPytania(aktualnePytanie.getId(),status);

        return "redirect:/robPowtorzenie?id="+aktualnePytanie.getPowtorzenie().getNazwa()+"&pk="+aktualnePytanie.getPowtorzenie().getNumer();
    }

    @RequestMapping(value = "/robPowtorzeniePodsumowaniePustego")
    public String robPowtorzeniePodsumowaniePustego(@RequestParam("zal") Integer zal, Model model)
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

        if (zal==2)
        {
            powtorzenia.resetujPowtorzenie(aktualnePytanie.getPowtorzenie());
            return "redirect:/pokarzPowtorzenia";
        }
        if(zal==3)
        {
            powtorzenia.dropPowotrzenie(aktualnePytanie.getPowtorzenie());
        }


        pytania.zatwierdzWykonaniePowtorzeniaPuste(aktualnePytanie.getPowtorzenie(),status);

        //nizej to samo co w pokarz powtorzenia
        List<Powtorzenie>powtorzeniaNaDzis=powtorzenia.getPowtorzeniaNaDzis();
        model.addAttribute("powtorzenia",powtorzeniaNaDzis);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
        return "redirect:/pokarzPowtorzenia";
    }

    @RequestMapping("/licznik")
    public String licz(Model model)
    {
        users.updateAktualnyUzytkownik();
        int liczbaDni=7;
        LocalDate dzis=LocalDate.now();
        Para liczby[]=new Para[liczbaDni];



        for (int i=1;i<=liczbaDni;i++) {
            liczby[i-1]=new Para();
            List<Powtorzenie> powtorzeniaNaTydzien = powtorzenia.getPowtorzeniaNaDzien(dzis.plusDays(i));
            liczby[i-1].setLiczba(powtorzeniaNaTydzien.size());
            powtorzeniaNaTydzien.clear();
            liczby[i-1].setNazwa(dzis.plusDays(i).getDayOfWeek().name());
        }
        model.addAttribute("liczbaPow",liczby);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
        return "pokarzPlan";
    }
    private boolean isNotSameSession()
    {
        if (aktualnePytanie.getPowtorzenie()==null)
            return true;
        else
            return false;
    }
}
