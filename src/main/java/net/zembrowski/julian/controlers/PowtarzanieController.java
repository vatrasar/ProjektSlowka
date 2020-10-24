package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.services.MediaSourceService;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Scope("session")
public class PowtarzanieController {

    @Autowired
    UzytkownikService users;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;

    @Autowired
    MediaSourceService mediaSourceService;
    @Autowired
    Pytanie aktualnePytanie;




    @RequestMapping(value = "/pokarzPowtorzenia")
    public String pokarzPowtorzenia(Model model)
    {
        users.updateAktualnyUzytkownik();

        List<Powtorzenie>powtorzeniaNaDzis=null;

        while(true)
        {
            powtorzeniaNaDzis=powtorzenia.getPowtorzeniaNaDzis();
            try {
                pytania.dropGhostRepetes(powtorzeniaNaDzis);
                break;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("numberOfQuestions",pytania.getQuestionsNumberForDay(powtorzeniaNaDzis));
        model.addAttribute("powtorzenia",powtorzeniaNaDzis);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
        model.addAttribute("classResolver", PowClassResolver.dark);

        model.addAttribute("questionClassResolver",QuestionClassResolver.easy);
        model.addAttribute("pytaniaService",pytania);
        return "pokarzPowtorzeniaDzis";
    }

    /**
     * inject to wykonywane actual repetition and next redirect
     * to fucntion witch makes normal repete steps
     * @param nazwa
     * @param numer
     * @param model
     * @return
     */
    @RequestMapping(value = "/robPowtorzenie")
    public String robPowtorzenie(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer, Model model)
    {

       if(users.updateAktualnyUzytkownik())
       {
           return "redirect:/pokarzPowtorzenia";
       }




       powtorzenia.ustawJakoAktualne(powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,users.getActualUserLogin())));
       return  "redirect:/proceseRepetition";
    }

    /**
     * check
     * a)is repetition empty
     * b)if not empty actualize actual question
     *next it start repeat question
     * @param model
     * @return
     */
    @RequestMapping(value = "/proceseRepetition")
    public String proceseRepetition(Model model)
    {


        Powtorzenie wykonywane=powtorzenia.getAktualnePowtorzenie();
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
                prepareModelAfterEndOfRepete(model);
                return "pokarzPowtorzeniaDzis";
            }
            //pytanie jest ustawiane jako odpowiedz a odpoweidz jako pytanie
            Pytanie pytanie=wykonywanePytania.get(0);
            prepareModelForQuestion(model,new Pytanie(),pytanie.reverse());

            //add media
            prepareModelForMedia(model, wykonywanePytania.get(0),MediaStatus.ANSWER);


            return "pytanie";

        }


        //pytanie to pytanie odpowiedz to odpowiedz
        prepareModelForQuestion(model, new Pytanie(), wykonywanePytania.get(0));

        if(wykonywanePytania.get(0).isNotion()) {
            //add media
            prepareModelForMedia(model, wykonywanePytania.get(0),MediaStatus.ANSWER);
            return "notion";
        }
        else {
            //add media
            prepareModelForMedia(model, wykonywanePytania.get(0),MediaStatus.QUESTION);
            return "pytanie";
        }
    }


    /**
     * add media only with MediaStatus like in status arg
     * @param model
     * @param currentQuestion
     * @param status
     */
    private void prepareModelForMedia(Model model, Pytanie currentQuestion,final MediaStatus status) {


        List<List<MediaSource>>madiaGroups=mediaSourceService.getMediaForQuestion(currentQuestion);


        mediaSourceService.filterWithStatus(madiaGroups,status);

        model.addAttribute("mediaImg",madiaGroups.get(0));
        model.addAttribute("mediaAudio",madiaGroups.get(1));
        model.addAttribute("mediaVideo",madiaGroups.get(2));
    }

    private void prepareModelForQuestion(Model model, Pytanie odpowiedz, Pytanie pytanie) {
        odpowiedz.setId(pytanie.getId());
        odpowiedz.setAnswer(pytanie.getAnswer());
        model.addAttribute("odp",odpowiedz);
        aktualnePytanie.setPytanie(pytanie);
        model.addAttribute("pyt",pytanie);
    }

    private void prepareModelAfterEndOfRepete(Model model) {
        model.addAttribute("powtorzono", true);
        //nizej to samo co w pokarz powtorzenia
        List<Powtorzenie> powtorzeniaNaDzis = powtorzenia.getPowtorzeniaNaDzis();
        model.addAttribute("numberOfQuestions",pytania.getQuestionsNumberForDay(powtorzeniaNaDzis));
        model.addAttribute("powtorzenia", powtorzeniaNaDzis);
        model.addAttribute("nazwaUzytkownika", users.getActualUserLogin());
        model.addAttribute("classResolver", PowClassResolver.dark);
        model.addAttribute("questionClassResolver",QuestionClassResolver.easy);
        model.addAttribute("pytaniaService",pytania);
    }


    @RequestMapping(value = "/robPowtorzenie",method = RequestMethod.POST)
    public String robPowtorzenieform(Pytanie odpowiedz, Model model)
    {
        model.addAttribute("isTraining",false);
        //pole pytanie w odpowiedzi zawiera teraz odpowiedz uzytkownika
        model.addAttribute("pytanie",odpowiedz);
        Pytanie pytanie=pytania.getPytanie(odpowiedz.getId());
        if(pytanie.getStatus()==Status.UMIEM_JEDNA_STRONE)
            prepareModelForMedia(model,pytanie,MediaStatus.QUESTION);
        else
            prepareModelForMedia(model,pytanie,MediaStatus.ANSWER);
        return "odpowiedz";
    }

    @RequestMapping(value = "/doNotion")
    public String doNotion(Model model)
    {
        if(users.updateAktualnyUzytkownik())
        {
            return "redirect:/pokarzPowtorzenia";
        }
        if (isNotSameSession())
        {
            return "redirect:/pokarzPowtorzenia";
        }
        Status status=Status.UMIEM;

        pytania.zmienStatusPytania(aktualnePytanie.getId(),status);

        return "redirect:/proceseRepetition";
    }
    @PostMapping(value = "/robPowtorzeniePodsumowanie")
    public String robPowtorzeniePodsumowanie(@RequestParam("zal") String zal, Model model)
    {
        if(users.updateAktualnyUzytkownik())
        {
            return "redirect:/pokarzPowtorzenia";
        }
        if (isNotSameSession())
        {
            return "redirect:/pokarzPowtorzenia";
        }
        Status status=pytania.determineStatus(zal,aktualnePytanie);

        pytania.zmienStatusPytania(aktualnePytanie.getId(),status);

        return "redirect:/proceseRepetition";
    }
    @RequestMapping(value = "/robPowtorzeniePodsumowanie")
    public String robPowtorzeniePodsumowanieForArch(@RequestParam("zal") String zal, Model model)
    {
        if(users.updateAktualnyUzytkownik())
        {
            return "redirect:/pokarzPowtorzenia";
        }
        if (isNotSameSession())
        {
            return "redirect:/pokarzPowtorzenia";
        }
        Status status=pytania.determineStatus(zal,aktualnePytanie);

        pytania.zmienStatusPytania(aktualnePytanie.getId(),status);

        return "redirect:/proceseRepetition";
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


    private boolean isNotSameSession()
    {
        if (aktualnePytanie.getPowtorzenie()==null)
            return true;
        else
            return false;
    }
}
