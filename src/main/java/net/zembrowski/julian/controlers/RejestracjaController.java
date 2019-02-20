package net.zembrowski.julian.controlers;


import com.sun.org.apache.xpath.internal.operations.Mod;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.repository.RoleRepository;
import net.zembrowski.julian.services.RoleServices;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.naming.Context;

@Controller
@Scope("session")
public class RejestracjaController {

    @Autowired
    UzytkownikService uzytkownicy;

    @Autowired
    RoleServices role;



    @RequestMapping(value ="/rejestruj",method = RequestMethod.POST)
    public String dojajUzytkownika(Uzytkownik nowyUzytkownik,Model model)
    {
        nowyUzytkownik.setEnabled(true);
         if(uzytkownicy.isExist(nowyUzytkownik))
         {
             model.addAttribute("error",true);
             model.addAttribute("urzytek",new Uzytkownik());
             return "rejestracja";
         }

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






}
