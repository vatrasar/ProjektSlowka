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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Scope("session")
public class TrainingControler {


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
    Pytanie aktualnePytanie;

   private Powtorzenie actualRepetition;

   private List<Powtorzenie>toLearn;

    public TrainingControler() {
        actualQuestionsList = null;
        toLearn=null;
    }

    public List<Powtorzenie> getToLearn() {
        return toLearn;
    }

    public void setToLearn(List<Powtorzenie> toLearn) {
        this.toLearn = toLearn;
    }

    @RequestMapping(value = "/repetsForTomorrow")
    public String repetsForTomorrow()
    {
        toLearn =powtorzenia.getRepetsToLearn();
        return "redirect:/training";
    }

    @RequestMapping(value = "/problematicQuests")
    public String problematicQuests(Model model)
    {

        actualQuestionsList=pytania.getProblematicQuestions(toLearn);
        return "redirect:/cwicz";
    }
    @RequestMapping(value = "/training")
   public String training(Model model) throws Exception
    {


       if(users.updateAktualnyUzytkownik())
       {
           return "redirect:/pokarzMenu";
       }

        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());

        model.addAttribute("powtorzono",false);
        model.addAttribute("classResolver", PowClassResolver.dark);
        model.addAttribute("pytService",pytania);
        model.addAttribute("classResolverExercises",new HtmlClassResolverExercises());

        if(actualRepetition==null)
        {
            actualRepetition=new Powtorzenie();
        }
        if (toLearn==null)
        {
            throw new Exception("toLearn is Empty!");
        }
        model.addAttribute("lastRepet",actualRepetition);
        model.addAttribute("powtorzenia",toLearn);
        return "pokarzDoPocwiczenia";
    }
    @RequestMapping(value = "/cwicz")
    public String work(Model model)
    {


       if (isNotSameSession())
       {
           return "redirect:/pokarzMenu";
       }

        //mieszanie pytań
        Collections.shuffle(actualQuestionsList);
        //jesli w powtorzeniu nie ma zadnych pytan to nic sie nie dzieje
        if (actualQuestionsList.isEmpty())
        {
            przygotujModel(model);
            model.addAttribute("powtorzono",true);
            model.addAttribute("lastRepet",actualRepetition);
            return "pokarzDoPocwiczenia";
        }
        actualQuestionsList=new ArrayList<>(actualQuestionsList);




        Pytanie pytanie=actualQuestionsList.get(0);
        prepareModelForQuestion(model,new Pytanie(),pytanie);

        if (pytanie.isNotion())
        {
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);
            return "notionTrening";
        }


        //add media
        if(pytanie.getStatus()==Status.UMIEM_JEDNA_STRONE)// test is using for question witch should to be reverse
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);
        else
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.QUESTION);




        return "pytanieCwicz";
    }

    @RequestMapping(value = "/cwiczOdp",method = RequestMethod.POST)
    public String cwiczOdpowiedz(Pytanie odpowiedz, Model model)
    {
        odpowiedz.setProblem(actualQuestionsList.get(0).isProblem());

        //pole pytanie w odpowiedzi zawiera teraz odpowiedz uzytkownika
        model.addAttribute("pytanie",odpowiedz);
        model.addAttribute("isTraining",true);
        model.addAttribute("isProblem",actualQuestionsList.get(0).isProblem());
        //add media
        if(actualQuestionsList.get(0).getStatus()==Status.UMIEM_JEDNA_STRONE)// test is use for question witch should to be reverse
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.QUESTION);
        else
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);


        return "odpowiedz";
    }


    @RequestMapping(value = "menuTraining")
   public String menuTraining(Model model,@RequestParam("id")String name,@RequestParam("pk") int number)
    {

        model.addAttribute("user",users.getActualUserLogin());
        Powtorzenie wykonywane=powtorzenia.getPowtorzenie(new Klucz(number,name,users.getActualUserLogin()));
        actualQuestionsList=pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);
        actualRepetition=wykonywane;

        return "trainingMenu";
    }
    public void prepareModelForQuestion(Model model, Pytanie odpowiedz, Pytanie pytanie) {
        odpowiedz.setId(pytanie.getId());
        odpowiedz.setAnswer(pytanie.getAnswer());
        model.addAttribute("odp",odpowiedz);
        aktualnePytanie.setPytanie(pytanie);
        model.addAttribute("pyt",pytanie);

        if(actualQuestionsList!=null)
            model.addAttribute("questionsNumber",actualQuestionsList.size());
        else
            model.addAttribute("questionsNumber",1);
        return;
    }

    /**
     * add media only with MediaStatus like in status arg
     * @param model
     * @param currentQuestion
     * @param status
     */
    public void prepareModelForMedia(Model model, Pytanie currentQuestion,final MediaStatus status) {
        List<MediaSource>mediaForQuestion=mediaSourceService.getMediaForQuestion(currentQuestion);

        List<List<MediaSource>>madiaGroups=mediaSourceService.groupByType(mediaForQuestion);


        mediaSourceService.filterWithStatus(madiaGroups,status);

        model.addAttribute("mediaImg",madiaGroups.get(0));
        model.addAttribute("mediaAudio",madiaGroups.get(1));
        model.addAttribute("mediaVideo",madiaGroups.get(2));
        return;
    }

    private boolean isNotSameSession()
    {
        if (actualQuestionsList==null)
            return true;
        else
            return false;
    }

    @PostMapping(value = "/cwiczPodsumowanie")
    public String cwiczPodsumowanie(@RequestParam("zal") String zal,Pytanie pytanie, Model model)
    {

        if (isNotSameSession())
        {
            return "redirect:/pokarzMenu";
        }

        pytania.updatePytanieProblem(pytanie.isProblem(),actualQuestionsList.get(0));
        if (zal.equals("Nie Umiem"))//nie umiem
        {


            //przesówanie pytania
            Pytanie nowe=actualQuestionsList.remove(0);
            if (actualQuestionsList.size()>6)
                actualQuestionsList.add(6,nowe);//on 6 place
            else
                actualQuestionsList.add(nowe);//na koniec listy

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

        if (pytanie.isNotion())
        {
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);
            return "notionTrening";
        }
        //add media
        if(pytanie.getStatus()==Status.UMIEM_JEDNA_STRONE)// test is use for question witch should to be reverse
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.ANSWER);
        else
            prepareModelForMedia(model, actualQuestionsList.get(0), MediaStatus.QUESTION);


        return "pytanieCwicz";
    }


   private void przygotujModel(Model model)
   {



        //nizej to samo co w pokarz powtorzenia
        model.addAttribute("powtorzenia",toLearn);
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());
       model.addAttribute("classResolver", PowClassResolver.dark);
       model.addAttribute("pytService",pytania);
       model.addAttribute("lastRepet",actualRepetition);
       model.addAttribute("classResolverExercises",new HtmlClassResolverExercises());

    }



    @RequestMapping(value = "/zaznacz")
    public String zaz(@RequestParam("id")String nazwa, @RequestParam("pk") Integer numer,@RequestParam("pow") boolean succesRepete, Model model)
    {
        users.updateAktualnyUzytkownik();

        setOpposedProblemInToLearnList(nazwa, numer);

        przygotujModel(model);
        model.addAttribute("powtorzono",succesRepete);


        powtorzenia.setOpposedProblem(new Klucz(numer,nazwa,users.getActualUserLogin()));

         return "pokarzDoPocwiczenia";


    }

    private void setOpposedProblemInToLearnList(@RequestParam("id") String nazwa, @RequestParam("pk") Integer numer) {
        int index=toLearn.indexOf(new Powtorzenie(nazwa,users.getActualUserLogin(),numer));
        Powtorzenie before=toLearn.get(index);
        before.setProblems(!before.isProblems());
    }

    public List<Pytanie> getActualQuestionsList() {
        return actualQuestionsList;
    }

    @RequestMapping(value = "retainProblems")
    public String retainProblems()
    {
        if(isNotSameSession())
        {
            return "redirect:/pokarzMenu";
        }
        actualQuestionsList=actualQuestionsList.stream().filter(a->a.isProblem()).collect(Collectors.toList());
        return "redirect:/cwicz";
    }

    /**
     * injects questions from marked repetitions and then redirect to training
     * @return
     */
    @RequestMapping("/trainingMarked")
    public String trainingMarked()
    {
        actualQuestionsList=pytania.getQuestionsOfMarked(toLearn);
        return "redirect:/cwicz";
    }

    public void setActualQuestionsList(List<Pytanie> actualQuestionsList) {
        this.actualQuestionsList = actualQuestionsList;
    }
}
