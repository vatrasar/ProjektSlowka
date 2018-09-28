package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.MediaStatus;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
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

import javax.persistence.NoResultException;
import java.util.List;
import java.util.logging.Logger;

@Controller
@Scope("session")
public class EditionControler {

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
        model.addAttribute("pytanie",new Pytanie());
        model.addAttribute("sukces",false);
        model.addAttribute("user",uytkownicy.getActualUserLogin());

        return  "addPytanieEdition";
    }

    @RequestMapping(value = "/pytanieAdd",method = RequestMethod.POST)
    @Transactional
    public String dodajPytanie(Pytanie nowe,@RequestParam("odp")MultipartFile[]plikiOdp,@RequestParam("ans")MultipartFile[]plikiAns)
    {
        nowe.setPowtorzenie(akutalnePowtorzenie);
        pytania.createPytanie(nowe, plikiAns, plikiOdp);



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
            return "redirect:/dodajPytanie";

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
    public String dropPytanie(Model model,@RequestParam("id") int id)
    {
        Powtorzenie pow=pytania.getPytanie(id).getPowtorzenie();
        pytania.deletePytanie(id);

        trainingControler.getActualQuestionsList().remove(0);
        return "redirect:/cwicz?id="+pow.getNazwa()+"&pk="+pow.getNumer();
    }
    @RequestMapping("/dropPytanieLast")
    public String dropPytanieLast(Model model,@RequestParam("id") int id)
    {

        pytania.deletePytanie(id);
        return "redirect:/pytanieAdd";
    }

    @RequestMapping(value = "/zmianaPytania",method = RequestMethod.POST)
    public String edycjaPytanie(Pytanie edytowane)
    {
        pytania.editPytanie(edytowane);
        return "redirect:/training";
    }

}