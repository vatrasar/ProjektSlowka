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
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
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
    final double IMAGE_SIZE=0.8;

    public void addNewProject(LatexProject newLatexProject)
    {
        newLatexProject.setUserName(users.getActualUserLogin());
        latexProjectRepository.addNewProject(newLatexProject);
    }

    public void generateLatexProject(LatexProject latexProject) {
        //key is repetition name, value repetitions with that name

        Map<String, List<Powtorzenie>>repetitionsByName=powtorzenia.getRepettionsByNameList(latexProject.getChaptersNames());
        StringBuilder document=new StringBuilder();
        document.append("\\maketitle").append(endl());
        document.append("\\tableofcontents").append(endl());
        int figuresCount=0;
        for(Map.Entry<String, List<Powtorzenie>> chapterElements:repetitionsByName.entrySet())
        {

            List<Pytanie>questions=pytanieServices.getPytaniaOfRepetitions(chapterElements.getValue());
            Map<String,List<Pytanie>>questionsGroupBySections=grupQuestionsBySectionName(questions);
            //add title
            StringBuilder chapter=new StringBuilder(packInLatexTag(CHAPTER,chapterElements.getKey())+endl());
            for(Map.Entry<String, List<Pytanie>> section:questionsGroupBySections.entrySet()) {
                //add questions
                chapter.append(packInLatexTag(SECTION,section.getKey())).append(endl());
                for (Pytanie question : questions) {
                    List<MediaSource> images = mediaSourceService.getMediaForQuestion(question).get(0);

                    String[] imagesStrings = getImagesStrings(figuresCount, chapter, images);
                    figuresCount += images.size();
                    //question title
                    String questionTitle = tagService.getQuestionTagsNames(question);
                    chapter.append(packInLatexTag(SUBSECTION, questionTitle)).append(endl());

                    //question
                    chapter.append(packInLatexTag(SUBSUBSECTION, "Pytanie"));
                    chapter.append(packInLatexTag(TEXT, question.getQuestion())).append(endl()).append(endl());
                    //ref to image
                    if (AreImagesfor(images, MediaStatus.QUESTION)) {
                        chapter.append(packInLatexTag(TEXT, imagesStrings[0])).append(endl()).append(endl());
                    }

                    chapter.append(packInLatexTag(SUBSUBSECTION, "Odpowiedz"));
                    //answer
                    if (question.getAnswer().length() > 0 && question.getAnswer().charAt(0) == '*') {
                        chapter.append(packInBeginBlock("lstlisting", mapPolishCharacters(question.getAnswer()))).append(endl()).append(endl());
                    } else {
                        chapter.append(question.getAnswer()).append(endl());
                    }
                    if (AreImagesfor(images, MediaStatus.ANSWER)) {
                        chapter.append(endl()).append(imagesStrings[1]).append(endl()).append(endl());
                        chapter.append(imagesStrings[2]).append(endl()).append(endl());
                    }


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
                PrintStream writer=new PrintStream("latexProject/out.tex","UTF-8");
                writer.print(resultDocument);
                PrintStream writerLatexKonf=new PrintStream("latexProject/latexKonf.tex","UTF-8");
                writerLatexKonf.print(getLatexKonfString());
                writerLatexKonf.close();
                writer.close();
                break;
            } catch (FileNotFoundException e) {
                try {
                    Files.createDirectories(Paths.get("latexProject"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }



    }

    private Map<String, List<Pytanie>> grupQuestionsBySectionName(List<Pytanie> questions) {
        return questions.stream().collect(groupingBy(Pytanie::getSectionName));
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
                    "\\includegraphics[width="+IMAGE_SIZE+"\\textwidth]{zdjecia/" +image.getFileName()+"}\n"+
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
