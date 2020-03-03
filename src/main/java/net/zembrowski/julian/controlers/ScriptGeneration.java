package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.LatexProject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ScriptGeneration {
    @RequestMapping("/scriptGereation")
    public String scriptGereation(Model model)
    {
        model.addAttribute("latexProject",new LatexProject());
        return "scriptGenerationPage";
    }
    @RequestMapping(value = "/makeLatexScript",method = RequestMethod.POST)
    public String makeLatexScript(LatexProject latexProject)
    {
        return "redirect:/pokarzMenu";
    }
}
