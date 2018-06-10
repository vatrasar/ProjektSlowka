package net.zembrowski.julian.controlers;


import net.zembrowski.julian.domain.Uzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuControler {

    @Autowired
    Uzytkownik akutalnyUzytkownik;
    @RequestMapping(value = "/pokarzMenu")
    String wyswietlMenu(Model model)
    {

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("nazwaUzytkownika",user.getUsername());
        akutalnyUzytkownik.setLogin(user.getUsername());

        return "menu";
    }
}
