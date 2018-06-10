package net.zembrowski.julian.controlers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuControler {

    @RequestMapping(value = "/pokarzMenu")
    String wyswietlMenu(Model model)
    {
        return "menu";
    }
}
