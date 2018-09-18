package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.services.MediaSourceService;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


@Controller
@Scope("session")
public class trainingControler {


    @Autowired
    UzytkownikService users;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;

    private  List<Pytanie>actualQuestionsList;

    @Autowired
    MediaSourceService mediaSourceService;

    @Autowired
    private Pytanie aktualnePytanie;

    @RequestMapping(value = "/training")
   public String training(Model model)
    {

        users.updateAktualnyUzytkownik();
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());

        List<Powtorzenie>toLearn =powtorzenia.getRepetsToLearn();
        model.addAttribute("powtorzenia",toLearn);

        return "pokarzDoPocwiczenia";
    }
    @RequestMapping(value = "/cwicz")
    public String work(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer, Model model)
    {


        Powtorzenie wykonywane=powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,users.getActualUserLogin()));
        actualQuestionsList=pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);

        //mieszanie pytań
        Collections.shuffle(actualQuestionsList);
        //jesli w powtorzeniu nie ma zadnych pytan to nic sie nie dzieje
        if (actualQuestionsList.isEmpty())
        {
            przygotujModel(model);
            model.addAttribute("powtorzono",true);
            return "pokarzDoPocwiczenia";
        }
        actualQuestionsList=new ArrayList<>(actualQuestionsList);




        Pytanie pytanie=actualQuestionsList.get(0);
        prepareModelForQuestion(model,new Pytanie(),pytanie);

        //add media
        if(pytanie.getStatus()==Status.UMIEM_JEDNA_STRONE)// test is use for question witch should to be reverse
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);
        else
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.QUESTION);


        return "pytanieCwicz";
    }

    @RequestMapping(value = "/cwiczOdp",method = RequestMethod.POST)
    public String cwiczOdpowiedz(Pytanie odpowiedz, Model model)
    {

        //pole pytanie w odpowiedzi zawiera teraz odpowiedz uzytkownika
        model.addAttribute("pytanie",odpowiedz);
        model.addAttribute("isTraining",true);
        //add media
        if(actualQuestionsList.get(0).getStatus()==Status.UMIEM_JEDNA_STRONE)// test is use for question witch should to be reverse
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.QUESTION);
        else
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);


        return "odpowiedz";
    }



    private void prepareModelForQuestion(Model model, Pytanie odpowiedz, Pytanie pytanie) {
        odpowiedz.setId(pytanie.getId());
        odpowiedz.setAnswer(pytanie.getAnswer());
        model.addAttribute("odp",odpowiedz);
        aktualnePytanie.setPytanie(pytanie);
        model.addAttribute("pyt",pytanie);

        return;
    }

    /**
     * add media only with MediaStatus like in status arg
     * @param model
     * @param currentQuestion
     * @param status
     */
    private void prepareModelForMedia(Model model, Pytanie currentQuestion,final MediaStatus status) {
        List<MediaSource>mediaForQuestion=mediaSourceService.getMediaForQuestion(currentQuestion);
        Logger.getGlobal().warning(mediaForQuestion.size()+"poczatek mediow");
        List<List<MediaSource>>madiaGroups=mediaSourceService.groupByType(mediaForQuestion);
        Logger.getGlobal().warning(madiaGroups.get(0).size()+"grupa img");

        mediaSourceService.filterWithStatus(madiaGroups,status);

        model.addAttribute("mediaImg",madiaGroups.get(0));
        model.addAttribute("mediaAudio",madiaGroups.get(1));
        model.addAttribute("mediaVideo",madiaGroups.get(2));
        return;
    }

    @RequestMapping(value = "/cwiczPodsumowanie")
    public String cwiczPodsumowanie(@RequestParam("zal") Integer zal, Model model)
    {

        if (zal==0)//nie umiem
        {


            //przesówanie pytania na koniec listy
            Pytanie nowe=actualQuestionsList.remove(0);
            actualQuestionsList.add(nowe);

        }
        else//umiem
        {
           Pytanie przetwarzany = actualQuestionsList.get(0);
            if (przetwarzany.getPowtorzenie().isReverse())
            {
                if (przetwarzany.getStatus()==Status.NIESPRAWDZONE) //umiem w jedna strone
                {

                    przetwarzany.setStatus(Status.UMIEM_JEDNA_STRONE);
                    przetwarzany.reverse();
                    //przesuwanie na koniec listy
                    actualQuestionsList.remove(0);
                    actualQuestionsList.add(przetwarzany);

                }
                else //usuwanie pytania które umiesz z listy
                {

                    actualQuestionsList.remove(0);

                }
            }
            else
                actualQuestionsList.remove(0);

        }


        return "redirect:/cwiczNext";
    }


    @RequestMapping(value = "/cwiczNext")
    public String workNext(Model model)
    {


        //jesli wszystko jest juz nauczone to konczysz powtarzaie
        if (actualQuestionsList.isEmpty())
        {

            przygotujModel(model);
            model.addAttribute("powtorzono",true);
            return "pokarzDoPocwiczenia";
        }




        Pytanie pytanie=actualQuestionsList.get(0);
        prepareModelForQuestion(model,new Pytanie(),pytanie);

        //add media
        if(pytanie.getStatus()==Status.UMIEM_JEDNA_STRONE)// test is use for question witch should to be reverse
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);
        else
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.QUESTION);


        return "pytanieCwicz";
    }


   private void przygotujModel(Model model)
   {


        List<Powtorzenie>toLearn =powtorzenia.getRepetsToLearn();
        //nizej to samo co w pokarz powtorzenia
        model.addAttribute("powtorzenia",toLearn);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
    }



    @RequestMapping(value = "/zaznacz")
    public String zaz(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer, Model model)
    {
        users.updateAktualnyUzytkownik();
        przygotujModel(model);
        powtorzenia.setOpposedProblem(new Klucz(numer,nazwa,users.getActualUserLogin()));
         return "pokarzDoPocwiczenia";


    }
}
