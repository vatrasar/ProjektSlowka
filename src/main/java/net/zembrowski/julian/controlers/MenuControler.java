package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.Uzytkownik;
import net.zembrowski.julian.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuControler {

    @Autowired
    UzytkownikService users;


    @RequestMapping(value = "/pokarzMenu")
   public String wyswietlMenu(Model model)
    {

        users.updateAktualnyUzytkownik();
        model.addAttribute("nazwaUzytkownika",users.getActualUserLogin());


        return "menu";
    }

}
