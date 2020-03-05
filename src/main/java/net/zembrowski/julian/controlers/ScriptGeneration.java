package net.zembrowski.julian.controlers;

import net.zembrowski.julian.domain.LatexProject;
import net.zembrowski.julian.services.LatexProjectService;
import net.zembrowski.julian.services.PowtorzenieServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Scope("session")
public class ScriptGeneration {

    @Autowired
    PowtorzenieServices repetitionsService;
    @Autowired
    LatexProjectService latexProjectService;
    @RequestMapping("/scriptGereation")
    public String scriptGereation(Model model)
    {
        model.addAttribute("latexProject",new LatexProject());
        return "scriptGenerationPage";
    }
    @RequestMapping(value = "/makeLatexScript",method = RequestMethod.PUT)
    public @ResponseBody String makeLatexScript(@RequestBody LatexProject latexProject)
    {
        latexProjectService.addNewProject(latexProject);
        return "ok";

    }

    @RequestMapping(method=RequestMethod.PUT, value="/searchChapters")
    public @ResponseBody List<String> searchChapters(@RequestBody String textContent)
    {
        List<String>result= repetitionsService.searchRepetitionsWithPartOfName(textContent);
        return result;
    }
}
