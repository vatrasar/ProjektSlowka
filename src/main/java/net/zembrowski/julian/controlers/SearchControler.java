package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.services.PowtorzenieServices;
import net.zembrowski.julian.services.PytanieServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.rmi.runtime.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
@Scope("session")
public class SearchControler {

    @Autowired
    UzytkownikService users;

    @Autowired
    PowtorzenieServices repetitions;
    @Autowired
    TrainingControler trainingControler;

    @RequestMapping("/search")
    public String search(Model model)
    {
        users.updateAktualnyUzytkownik();
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());

        return "searchConditions";
    }

    @PostMapping("/search")
    public String searchResults(Model model, @RequestParam("name")String name, @RequestParam("date")String date)
    {


        setPowToLearn(name, date);

        return "redirect:/training";
    }

    private void setPowToLearn(String name,String date) {



        List<List<Powtorzenie>>conditionsList=new ArrayList<>();
        setConditionsLists(name, date, conditionsList);

        List<Powtorzenie>resultList;
        if (conditionsList.size()!=0)
            resultList=new ArrayList<>(conditionsList.get(0));
        else
            resultList=new ArrayList<>();
        intersectionsOfLists(conditionsList, resultList);

        trainingControler.setToLearn(resultList);
    }

    private void intersectionsOfLists(List<List<Powtorzenie>> conditionsList, List<Powtorzenie> resultList) {
        for (List<Powtorzenie>condition:conditionsList)
         {
             resultList.retainAll(condition);
         }
    }

    /**
     * for each condition, function add to the list repetitions which fulfills that condition
     * @param name
     * @param date
     * @param powList
     */
    private void setConditionsLists(String name, String date, List<List<Powtorzenie>> powList) {

        LocalDate repetitionDate=parseDate(date);

        if(!name.equals(""))
            powList.add(repetitions.getPowtorzeniaByName(name));
        if(!date.equals(""))
        {
            powList.add(repetitions.getPowtorzeniaByDate(repetitionDate));
        }
    }

    private LocalDate parseDate(String date) {

        if(date.equals(""))
            return null;
        LocalDate repetitionDate;DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        repetitionDate=LocalDate.parse(date,formatter);
        return repetitionDate;
    }
}
