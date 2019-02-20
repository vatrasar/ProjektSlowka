package net.zembrowski.julian.controlers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorCoontroller implements ErrorController {

    @RequestMapping(value = "/error")
    public String handler()
    {
        return "errorPage";
    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
