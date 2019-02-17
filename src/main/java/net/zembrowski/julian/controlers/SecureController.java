package net.zembrowski.julian.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecureController {

    @RequestMapping(value = "/login")
    public String login()
    {
        return "login";
    }

    @RequestMapping(value = "/badLogin")
    public String badLogin(Model model)
    {
        model.addAttribute("error",true);
        return "login";
    }

}
