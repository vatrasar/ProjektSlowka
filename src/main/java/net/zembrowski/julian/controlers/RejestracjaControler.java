package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.RoleRepository;
import net.zembrowski.julian.services.RoleServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RejestracjaControler {

    @Autowired
    UzytkownikService uzytkownicy;

    @Autowired
    RoleServices role;


    @RequestMapping(value ="/rejestruj",method = RequestMethod.POST)
    public String dojajUzytkownikarejestruj(Uzytkownik nowyUzytkownik)
    {
        uzytkownicy.addUzytkownik(nowyUzytkownik);
        role.persistRole(nowyUzytkownik.getLogin(),"user");
        return "redirect:/hello";
    }

    @RequestMapping(value ="/rejestruj")
    public String rejestruj(Model model)
    {

        model.addAttribute("urzytek",new Uzytkownik());
        return "rejestracja";
    }

    @RequestMapping(value ="/hello")
    public String powitanie(Model model)
    {
        return "hello";
    }


}
