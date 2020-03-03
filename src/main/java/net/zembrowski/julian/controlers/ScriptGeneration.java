package net.zembrowski.julian.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ScriptGeneration {
    @RequestMapping("/scriptGereation")
    public String scriptGereation(Model model)
    {
        return "scriptGenerationPage";
    }
}
