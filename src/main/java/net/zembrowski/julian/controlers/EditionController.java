package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.TagService;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.logging.Logger;

@Controller
@Scope("session")
public class EditionController {

    @Autowired
    UzytkownikService uytkownicy;
    @Autowired
    Powtorzenie akutalnePowtorzenie;
    @Autowired
    PowtorzenieServices powtorzenia;
    @Autowired
    PytanieServices pytania;
    @Autowired
    TrainingControler trainingControler;

    @Autowired
    TagService tagService;





    @RequestMapping(value="/menuEdycji")
    public String menu(@RequestParam(name = "id")String nazwa, @RequestParam(name = "pk")int numer, Model model,@RequestParam(name = "source")String source)
    {

        powtorzenia.ustawJakoAktualne(powtorzenia.getPowtorzenie(new Klucz(numer,nazwa,uytkownicy.getActualUserLogin())));


       if (source.equals("training"))
        {
            if (akutalnePowtorzenie.isEmpty()) {
                model.addAttribute("puste", true);

            }
            else {
                model.addAttribute("puste", false);

            }
            uytkownicy.updateAktualnyUzytkownik();
            model.addAttribute("user",uytkownicy.getActualUserLogin());

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
        model.addAttribute("pytanie",new Pytanie(akutalnePowtorzenie));
        model.addAttribute("repetitionSize",pytania.getPytaniaPowtorzenia(akutalnePowtorzenie).size());
        model.addAttribute("user",uytkownicy.getActualUserLogin());

        return  "addPytanieEdition";
    }

    @RequestMapping(value = "/takeQuestionsList")
    public String takeQuestionsList(String questionsList,Model model)
    {
        uytkownicy.updateAktualnyUzytkownik();
        model.addAttribute("user",uytkownicy.getActualUserLogin());
        model.addAttribute("questionsList", new Pytanie());
        return  "takeQuestionsList";
    }



    @PostMapping(value = "/addQuestionsFromList")
    public String addQuestionsFromList(Pytanie questionsList,Model model)
    {
        uytkownicy.updateAktualnyUzytkownik();
        model.addAttribute("user",uytkownicy.getActualUserLogin());
        pytania.addQuestionsList(akutalnePowtorzenie,questionsList.getAnswer());
        model.addAttribute("questionsList",new Pytanie());
        model.addAttribute("sucessAdd",true);
        return  "takeQuestionsList";

    }

    @RequestMapping(value = "/pytanieAdd",method = RequestMethod.POST)
    public String dodajPytanie(Pytanie nowe,@RequestParam("odp")MultipartFile[]plikiOdp,@RequestParam("ans")MultipartFile[]plikiAns,@RequestParam("tagi")String tags)
    {
        nowe.setPowtorzenie(akutalnePowtorzenie);
        pytania.createPytanie(nowe, plikiAns, plikiOdp,tags);

        return  "redirect:/pytanieAdd";
    }
    @RequestMapping("/dropPow")
    public String dropPow()
    {
        pytania.dropPytaniaOfPowtorzenie(akutalnePowtorzenie);
        tagService.dropTagsOfRepetition(akutalnePowtorzenie);
        List<Powtorzenie>toLearn=powtorzenia.getActualRepetitions();
        int index=toLearn.indexOf(akutalnePowtorzenie);
        toLearn.remove(index);
        powtorzenia.dropPowotrzenie(akutalnePowtorzenie);
        return "redirect:/training";
    }
    @RequestMapping("/archPow")
    public String archPow()
    {
        powtorzenia.archPow(akutalnePowtorzenie);
        return "redirect:/repetsForTomorrow";
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

        //There was SQLExpection without that
       Powtorzenie temp=pytania.addQuestionsToTempPowotrzenie(pytaniaStaregoPowtorzenia);


        //usuniecie pierwotnego powtorzenia by sprawdzic czy nowe już gdzieś nie istnieje
        powtorzenia.dropPowotrzenie(akutalnePowtorzenie);

        //jeśli nazwa jest ta sama co poprzednio nie powinno sie zmieniać numeru powtorzenia
        if(akutalnePowtorzenie.getNazwa().equals(NowePowtorzenie.getNazwa()))
            NowePowtorzenie.setNumer(akutalnePowtorzenie.getNumer());
        else
            NowePowtorzenie.setNumer(powtorzenia.getMaxNumer(NowePowtorzenie.getNazwa())+1);


        if(powtorzenia.isExist(NowePowtorzenie))
        {
            powtorzenia.persistPowtorzenie(akutalnePowtorzenie);
            pytania.addPytaniaToPowtorzenie(akutalnePowtorzenie,pytaniaStaregoPowtorzenia);
            //czyszczenie
            powtorzenia.dropPowotrzenie(temp);
            return "/pokarzMenu";
        }

        powtorzenia.persistPowtorzenie(NowePowtorzenie);

        if(!NowePowtorzenie.isEmpty()) {

            pytania.addPytaniaToPowtorzenie(NowePowtorzenie, pytaniaStaregoPowtorzenia);

        }
        //czyszczenie
        powtorzenia.dropPowotrzenie(temp);
        powtorzenia.ustawJakoAktualne(NowePowtorzenie);

        if (NowePowtorzenie.isEmpty())
        {
            model.addAttribute("nowePow",NowePowtorzenie);
            return "podsumowanieDodanegoPowtorzenia";
        }
        else
            return "redirect:/pytanieAdd";

    }

    @RequestMapping("showLast")
    public String showLastQuestion(Model model)
    {
        Pytanie lastAddedQuestion= null;
        try {
            lastAddedQuestion = pytania.getLastAdded(akutalnePowtorzenie);
        } catch (NoResultException e) {

            return "redirect:/pytanieAdd";
        }




        trainingControler.prepareModelForQuestion(model,new Pytanie(),lastAddedQuestion);
        trainingControler.prepareModelForMedia(model, lastAddedQuestion, MediaStatus.QUESTION);
        return "lastQuest";
    }

    @PostMapping("showLast")
    public String showLastAnswer(Model model)
    {


        Pytanie lastAddedQuestion=pytania.getLastAdded(akutalnePowtorzenie);

        model.addAttribute("pytanie",lastAddedQuestion);
        trainingControler.prepareModelForMedia(model, lastAddedQuestion, MediaStatus.ANSWER);
        return "lastAnswer";
    }


    @RequestMapping("/dropPytanie")
    public @ResponseBody String dropPytanie(Model model, @RequestParam("id") int id)
    {
        if(id==-1)//when on page no informations about question id it returns -1. mostly condition should be true
        {
            id=pytania.getActualQuestionsList().get(0).getId();
        }
        Powtorzenie pow=pytania.getPytanie(id).getPowtorzenie();
        pytania.deletePytanie(id);

        pytania.getActualQuestionsList().remove(0);
        return "";

    }
    @RequestMapping("/dropPytanieLast")
    public String dropPytanieLast(Model model,@RequestParam("id") int id)
    {

        pytania.deletePytanie(id);
        return "redirect:/pytanieAdd";
    }
    @RequestMapping("/zmianaPytania")
    public String zmienPytanie(@RequestParam("id") int id, Model model)
    {
        Pytanie edytowane=new Pytanie(pytania.getPytanie(id));

        String tags=tagService.getQuestionTagsNames(edytowane);
        model.addAttribute("pytanie",edytowane);
        model.addAttribute("edition",true);
        model.addAttribute("tags",tags);
        return "pytanieEdition";
    }

    @RequestMapping(value = "/zmianaPytania",method = RequestMethod.POST)
    public String edycjaPytanie(Pytanie edytowane,@RequestParam("newTags")String newTags,@RequestParam("odp")MultipartFile[]plikiOdp,@RequestParam("ans")MultipartFile[]plikiQuest)
    {
        tagService.editTags(pytania.getPytanie(edytowane.getId()),newTags);
        pytania.editPytanie(edytowane,plikiQuest,plikiOdp);

        if (pytania.getActualQuestionsList()==null || pytania.getActualQuestionsList().isEmpty())
            return "redirect:/pokarzMenu";
        else
           return "redirect:/cwiczNext";
    }

    @RequestMapping("/collectMarked")
    public String collectMarked()
    {
        pytania.collectMarked(powtorzenia.getActualRepetitions());
        return "redirect:/repetsForTomorrow";

    }

}
