package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.dto.LatexProjectInfo;
import net.zembrowski.julian.repository.LatexProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Media;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

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
    @Autowired
    MediaSourceService mediaSourceService;


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
        int figuresCount=0;
        for(Map.Entry<String, List<Powtorzenie>> chapterElements:repetitionsByName.entrySet())
        {

            List<Pytanie>questions=pytanieServices.getPytaniaOfRepetitions(chapterElements.getValue());
            //add title
            StringBuilder chapter=new StringBuilder(packInLatexTag(SECTION,chapterElements.getKey())+endl());

            //add questions
            for(Pytanie question:questions)
            {
                List<MediaSource> images=mediaSourceService.getMediaForQuestion(question).get(0);

                String[]imagesStrings=getImagesStrings(figuresCount, chapter,images);
                figuresCount +=images.size();
                //question title
               String questionTitle=tagService.getQuestionTagsNames(question);
               chapter.append(packInLatexTag(SUBSECTION,questionTitle)).append(endl());

                //question

               chapter.append(packInLatexTag(TEXT,question.getQuestion())).append(endl()).append(endl());
               if(AreImagesfor(images,MediaStatus.QUESTION)) {
                   chapter.append(packInLatexTag(TEXT, imagesStrings[0])).append(endl()).append(endl());//ref to image
               }

                //answer
                if(question.getAnswer().length()>0 && question.getAnswer().charAt(0)=='*')
                {
                    chapter.append(packInBeginBlock("lstlisting",question.getAnswer())).append(endl()).append(endl());

                }
                else
                {
                    chapter.append(question.getAnswer()).append(endl());
                }
                if(AreImagesfor(images,MediaStatus.ANSWER)) {
                    chapter.append(endl()).append(imagesStrings[1]).append(endl()).append(endl());
                    chapter.append(imagesStrings[2]).append(endl()).append(endl());
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
        while (true)
        {
            try {
                PrintStream writer=new PrintStream("latexProject/out.tex");
                writer.print(resultDocument);
                writer.close();
                break;
            } catch (FileNotFoundException e) {
                try {
                    Files.createDirectories(Paths.get("latexProject/zdjecia"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }



    }

    private boolean AreImagesfor(List<MediaSource> images,MediaStatus mediaType) {
        for(MediaSource mediaSource:images)
        {
            if(mediaSource.getStatus()==mediaType)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * add images to question
     * @param figuresCount
     * @param chapter
     * @param images
     * @return [0] ref to images form question
     * [1] ref to images form answer
     * [3] figures block
     */
    private String[] getImagesStrings(int figuresCount, StringBuilder chapter,  List<MediaSource> images) {

        StringBuilder refToImagesOfQuestion=new StringBuilder();
        StringBuilder refToImagesOfAnswer=new StringBuilder();
        StringBuilder figuresBlock=new StringBuilder();
        refToImagesOfQuestion.append("Grafiki dla pytania to:");
        refToImagesOfAnswer.append("Grafiki dla odpowiedzi to:");
        for(MediaSource image:images)
        {
            copyFile(image.getPath());
            figuresCount+=1;
            figuresBlock.append("\\begin{figure}\n" +
                    "\n" +
                    "\\caption{}\n" +
                    "\\label{zdj:"+ figuresCount+"}\n"+
                    "\\centering\n" +
                    "\\includegraphics[width=\\textwidth]{zdjecia/" +image.getFileName()+"}\n"+
                    "\\end{figure}");
            if(image.getStatus()== MediaStatus.ANSWER)
                refToImagesOfAnswer.append(" ").append(packInLatexTag("ref","zdj:"+figuresCount)).append(" ");
            else {
                refToImagesOfAnswer.append(" ").append(packInLatexTag("ref","zdj:"+figuresCount)).append(" ");
            }


        }

        return new String[]{refToImagesOfQuestion.toString(),refToImagesOfAnswer.toString(),figuresBlock.toString()};
    }


    public List<LatexProjectInfo> getLatexProjectsInfoList() {
        List<LatexProject>latexProjectList= latexProjectRepository.getLatexProjectList(users.getActualUserLogin());

        return latexProjectList.stream().map((latexProject)->new LatexProjectInfo(latexProject)).collect(Collectors.toList());
    }

    public LatexProject getLatexProject(int projectId) {
        return latexProjectRepository.getLatexProject(projectId);
    }

    public void updateProject(LatexProject latexProject) {
        latexProject.setUserName(users.getActualUserLogin());
        latexProjectRepository.updateProject(latexProject);
    }

    public void dropProject(Integer projectId) {
        latexProjectRepository.dropProject(projectId);
    }
}
