package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.LatexProject;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.repository.LatexProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static net.zembrowski.julian.Utils.LatexGenerationUtils.*;

@Service
@Scope("session")
public class LatexProjectService {
    @Autowired
    LatexProjectRepository latexProjectRepository;
    @Autowired
    UzytkownikService users;
    @Autowired
    PytanieServices pytanieServices;
    @Autowired
    private PowtorzenieServices powtorzenia;
    @Autowired
    TagService tagService;


    public void addNewProject(LatexProject newLatexProject)
    {
        newLatexProject.setUserName(users.getActualUserLogin());
        latexProjectRepository.addNewProject(newLatexProject);
    }

    public void generateLatexProject(LatexProject latexProject) {
        //key is repetition name, value repetitions with that name
        Map<String, List<Powtorzenie>>repetitionsByName=powtorzenia.getRepettionsByNameList(latexProject.getChaptersNames());
        StringBuilder document=new StringBuilder();
        document.append("\\maketitle");
        for(Map.Entry<String, List<Powtorzenie>> chapterElements:repetitionsByName.entrySet())
        {

            List<Pytanie>questions=pytanieServices.getPytaniaOfRepetitions(chapterElements.getValue());
            //add title
            StringBuilder chapter=new StringBuilder(packInLatexTag(SECTION,chapterElements.getKey())+endl());

            //add questions
            for(Pytanie question:questions)
            {
                //question title
               String questionTitle=tagService.getQuestionTagsNames(question);
               chapter.append(packInLatexTag(SUBSECTION,questionTitle)).append(endl());

                //question

               chapter.append(packInLatexTag(TEXT,question.getQuestion())).append(endl()).append(endl());

                //answer
                if(question.getAnswer().length()>0 && question.getAnswer().charAt(0)=='*')
                {
                    chapter.append(packInBeginBlock("lstlisting",question.getAnswer())).append(endl()).append(endl());

                }
                else
                {
                    chapter.append(question.getAnswer()).append(endl());
                }


            }

            document.append(chapter.toString());

        }

        //head
        StringBuilder documentHead=new StringBuilder();
        //cofiguration
        documentHead.append(packInLatexTag("input","latexKonf")).append(endl());
        documentHead.append(packInLatexTag("title",latexProject.getProjectName())).append(endl());

        String resultDocument=documentHead.toString()+packInBeginBlock("document",document.toString());
        try {
            PrintStream writer=new PrintStream("out.tex");
            writer.print(resultDocument);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
