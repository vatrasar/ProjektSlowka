package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.repository.PytanieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class PytanieServices {

    @Autowired
    private PytanieRepository pytania;
    @Autowired
    private PowtorzenieServices powtorzenia;

    @Autowired
    private UzytkownikService users;

    @Autowired
    MediaSourceService mediaSourceService;
    @Autowired
    TagService tagService;

    private  List<Pytanie>actualQuestionsList;
    private static String path="programDane/";//path to folder with video and img data in src(to save data)
    //on google Drive
    private static String pathBackup=null;


   public PytanieServices()
   {
       actualQuestionsList=null;
   }

    public void createPytanie(Pytanie nowePytanie, MultipartFile[] plikiPyt, MultipartFile[] plikiOdp, String tags)
    {
        try {
            upadateLastAdded(nowePytanie);
            pytania.createPytanie(nowePytanie);

            tagService.addQuestionTags(nowePytanie, tags);
            saveFiles(plikiOdp, MediaStatus.ANSWER, nowePytanie);
            saveFiles(plikiPyt, MediaStatus.QUESTION, nowePytanie);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void upadateLastAdded(Pytanie newLast) {
        newLast.setLastAdded(true);
        Pytanie last= null;
        try {
            last = getLastAdded(newLast.getPowtorzenie());
            last.setLastAdded(false);
            pytania.upadatePytanie(last);
        } catch (NoResultException e) {

            return;
        }


    }

    public Pytanie getLastAdded(Powtorzenie powtorzenie)throws javax.persistence.NoResultException {

        return pytania.getLastAdded(powtorzenie);
    }

    /**
     * saev files to directory and their paths to database
     * @param pliki
     * @param status
     * @param nowePytanie
     */
    private void saveFiles(MultipartFile[] pliki, MediaStatus status, Pytanie nowePytanie) {

       if(pathBackup==null)
       {
           pathBackup=getBackupDirectory();
       }

        for (MultipartFile plik:pliki)
        {
            if (plik.getOriginalFilename().length()<4)//if name of file isn't right(mostly when there is no file)
            {
                continue;
            }
            Path sciezkaSrc= Paths.get(path,(mediaSourceService.getMaxId()+1)+plik.getOriginalFilename().substring(plik.getOriginalFilename().length()-4,plik.getOriginalFilename().length()));
            Path sciezkaBackup=Paths.get(pathBackup,(mediaSourceService.getMaxId()+1)+plik.getOriginalFilename().substring(plik.getOriginalFilename().length()-4,plik.getOriginalFilename().length()));
            try {

                Files.write(sciezkaSrc,plik.getBytes());
                Files.write(sciezkaBackup,plik.getBytes());
                mediaSourceService.persistanceMediaSource(new MediaSource("/programDane"+"/"+(mediaSourceService.getMaxId()+1)+plik.getOriginalFilename().substring(plik.getOriginalFilename().length()-4,plik.getOriginalFilename().length()),nowePytanie,status));
            }
            catch (java.nio.file.AccessDeniedException e)
            {
                Logger.getGlobal().info("zakonczono wczytywanie plikow");
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private String getBackupDirectory() {


        try {
            Scanner in=new Scanner(new File("backupDirectory.txt"));
            String result=in.nextLine();

            Logger.getGlobal().warning(result);
            in.close();
            return result;

        } catch (Exception e) {
            System.out.println("WWWWWWWWWWWWWWWWWWWWOOO = " +
                    System.getProperty("user.dir"));
            e.printStackTrace();
        }


        return "";
    }

    public List<Pytanie> getPytaniaPowtorzeniaNiesprawdzone(Powtorzenie wykonywane) {

       return pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);


    }

    /**
     * inject Unverified questions to actualQuestinsList
     * @param processed
     */
    public void injectUnverified(Powtorzenie processed) {

        actualQuestionsList=pytania.getPytaniaPowtorzeniaNiesprawdzone(processed);


    }

    @Transactional
    public void zatwierdzWykonaniePowtorzenia(Powtorzenie wykonywane) {

        List<Pytanie>bledy=pytania.getBledy(wykonywane);
        //set problem off
        bledy.forEach(a->a.setProblem(false));
        List<Pytanie>hardQuestions=pytania.getHardQuestions(bledy);//question is hard when has bad statistics
        //uzyskanie numeru jakie powinno miec powtorzenie z bledami
        int numer = 1 + powtorzenia.getMaxNumer(wykonywane.getNazwa());
        if(!hardQuestions.isEmpty())
        {

            int numberForHard=numer+1;
            bledy.removeAll(hardQuestions);
            createRepetitionForFaults(wykonywane, hardQuestions, numberForHard, true);
        }

        if(bledy.size()!=0) {

            //tworzymy nowe powtorzenie dla bledów
            createRepetitionForFaults(wykonywane, bledy, numer, false);

        }
        wykonywane.refaktoryzujPowtorzenie();
        powtorzenia.updatePowtorzenie(wykonywane);
        pytania.nadajStatusNiesprawdzone(wykonywane);


    }

    private void pushStatistic(Pytanie a, int next) {
        a.pushStatistic(next);
        pytania.upadatePytanie(a);
    }

    private Powtorzenie createRepetitionForFaults(Powtorzenie oldRepetition, List<Pytanie> questions, int idNumber, boolean hard) {
        final int next=oldRepetition.getNastepne();
        questions.forEach(a->pushStatistic(a,next));
        Powtorzenie repetiotionForFaults = new Powtorzenie();
        repetiotionForFaults.utworzPowDlaBledow(oldRepetition, idNumber, hard);
        powtorzenia.persistPowtorzenie(repetiotionForFaults);
        tagService.copyGlobalTags(oldRepetition, repetiotionForFaults);
        //dodaj bledy do danego powtorzenia
        for (Pytanie modyfikowane : questions) {
            pytania.dodajDoPowtorzenia(modyfikowane, repetiotionForFaults);

        }
        pytania.nadajStatusNiesprawdzone(repetiotionForFaults);
        return repetiotionForFaults;
    }

    /**
     * Jesli czyNauczone ma wartość UMIEM to nastepne zostaje podniesione o jeden poziom(nap z 1 na 3) i data zmieniona analogicznie do poziomu(np przesunieta o trzy dni od dzisiaj)
     * jeśli czyNauczone ma wartość NIEUMIEM to zostanie utworzone nowe powtórzenie które zostanie
     * ustawione na nastepny dzień(jutro).Natomiast stare powtórzenie zostaje potraktowane tak jakby zostało
     * poprawnie wykonane.
     * @param wykonywane
     * @param czyNauczone
     */
    @Transactional
    public void zatwierdzWykonaniePowtorzeniaPuste(Powtorzenie wykonywane,Status czyNauczone) {


        if(czyNauczone==Status.NIEUMIEM) {
            //uzyskanie numeru jakie powinno miec powtorzenie z bledami
            int numer = 1 + powtorzenia.getMaxNumer(wykonywane.getNazwa());

            //tworzymy nowe powtorzenie dla bledów
            Powtorzenie powDlaBledow = new Powtorzenie();
            powDlaBledow.utworzPowDlaBledow(wykonywane, numer,false);

            powDlaBledow.setEmpty(true);
            //dodanie nowego powtrzenia do bazy
            powtorzenia.persistPowtorzenie(powDlaBledow);

        }
        wykonywane.refaktoryzujPowtorzenie();
        powtorzenia.updatePowtorzenie(wykonywane);

    }


    public boolean hasPowtorzenieProblems(Powtorzenie pow)
    {
        List<Pytanie>repeteQuests=pytania.getPytaniaOfPowtorzenie(pow);
        repeteQuests=repeteQuests.stream().filter(a->a.isProblem()).collect(Collectors.toList());
        if (repeteQuests.size()!=0)
            return true;
        else
            return false;
    }

    @Transactional
    public void zmienStatusPytania(Integer id, Status status) {
        pytania.zmienStatusPytania(id,status);
    }

    @Transactional
    public void deletePytanie(int id) {
        mediaSourceService.dropMediaOfPytanie(pytania.getPytanie(id));
        tagService.dropTagsOfPytanie(pytania.getPytanie(id));
        pytania.deletePytanie(id);
    }

    @Transactional
    public void editPytanie(Pytanie edytowane, MultipartFile[] plikiQuest, MultipartFile[] plikiOdp) {

        Pytanie stare=pytania.getPytanie(edytowane.getId());
        edytowane.setPowtorzenie(stare.getPowtorzenie());
        edytowane.setStatus(stare.getStatus());
        edytowane.setLastAdded(stare.isLastAdded());
        Pytanie nowe=new Pytanie(edytowane);
        pytania.upadatePytanie(nowe);

        saveFiles(plikiOdp, MediaStatus.ANSWER,nowe);
        saveFiles(plikiQuest, MediaStatus.QUESTION, nowe);

        
    }


    @Transactional
    public void dropPytaniaOfPowtorzenie(Powtorzenie powtorzenie) {

        List<Pytanie>pytaniaPowtorzenia=pytania.getPytaniaOfPowtorzenie(powtorzenie);
        for(Pytanie pytanie:pytaniaPowtorzenia)
        {
            deletePytanie(pytanie.getId());
        }

    }

    public List<Pytanie> getPytaniaPowtorzenia(Powtorzenie powtorzenie) {

        return pytania.getPytaniaOfPowtorzenie(powtorzenie);
    }

    /**
     * it's only to upadte data in database, not to add new questions!
     * to add new question use create question
     * @param nowe
     * @param pytaniaStaregoPowtorzenia
     */
    public void addPytaniaToPowtorzenie(Powtorzenie nowe, List<Pytanie> pytaniaStaregoPowtorzenia) {

        for(Pytanie pyt:pytaniaStaregoPowtorzenia)
        {
            pyt.setPowtorzenie(nowe);
            pytania.upadatePytanie(pyt);
        }
    }


    /**
     * Check witch status should question should get now.
     * Warning! Only for question from reversed repete!
     * @param zal
     * @param aktualne
     * @return
     */
    public Status determineStatus(String zal, Pytanie aktualne) {


        if (zal.equals("Nie Umiem"))
        {
           return Status.NIEUMIEM;
        }
        if(aktualne.getPowtorzenie().isReverse())
        {


            if (aktualne.getStatus()==Status.UMIEM_JEDNA_STRONE)
            {
                return Status.UMIEM;
            }
            else
                return Status.UMIEM_JEDNA_STRONE;

        }
        else
            return Status.UMIEM;

    }

    public List<Pytanie> getOneWayCheckedQuestions(Powtorzenie wykonywane) {


        return pytania.getOneWayCheckedQuestions(wykonywane);

    }

    public Powtorzenie addQuestionsToTempPowotrzenie(List<Pytanie> pytaniaStaregoPowtorzenia) {

        Powtorzenie temp=new Powtorzenie();
        temp.setNazwa("temp");
        temp.setWlasciciel(users.getActualUserLogin());
        temp.setNumer(-1);//number not posible for ordinary user
        powtorzenia.persistPowtorzenie(temp);
        this.addPytaniaToPowtorzenie(temp,pytaniaStaregoPowtorzenia);
        return temp;
    }

    public Pytanie getPytanie(int id) {
        return pytania.getPytanie(id);
    }

    /**
     * check if repete isn't mark as empty.then if it is true,
     * method checks how many questions has that repete.
     * if it is zero, method deletes repete
     * @param powtorzeniaNaDzis
     */
    @Transactional
    public void dropGhostRepetes(List<Powtorzenie> powtorzeniaNaDzis)
    {

        List<Powtorzenie>toDrop=new ArrayList<>();
        for (Powtorzenie checkRepete : powtorzeniaNaDzis)
        {
            if(!checkRepete.isEmpty()) {
                int repeteSize = pytania.getPytaniaOfPowtorzenie(checkRepete).size();
                if (repeteSize == 0) {


                    toDrop.add(checkRepete);
                    powtorzenia.dropPowotrzenie(checkRepete);


                }
            }
        }
        powtorzeniaNaDzis.removeAll(toDrop);


    }



    public void updatePytanieProblem(boolean prob, Pytanie pytanie) {


        pytanie.setProblem(prob);


       Pytanie mergeQuestion=pytania.getPytanie(pytanie.getId());
       mergeQuestion.setProblem(prob);
        pytania.upadatePytanie(mergeQuestion);
    }


    /**
     *
     * @param toLearn
     * @return all questions with problem set on true.
     */
    public List<Pytanie> getProblematicQuestions(List<Powtorzenie> toLearn) {

        List<Pytanie>result=new ArrayList<>();

        for (Powtorzenie pow:toLearn)
        {
            List<Pytanie>questions=pytania.getPytaniaOfPowtorzenie(pow);
            questions=questions.stream().filter(a->a.isProblem()).collect(Collectors.toList());

            result.addAll(questions);
        }
        return result;
    }

    /**
     * inject problematic questions to actualQuestionsList
     */
    public void injectProblematic() {
        actualQuestionsList=getProblematicQuestions(powtorzenia.getActualRepetitions());
    }

    public List<Pytanie> getQuestionsOfMarked(List<Powtorzenie> toLearn) {

        toLearn=toLearn.stream().filter(Powtorzenie::isProblems).collect(Collectors.toList());
        return getPytaniaOfRepetitions(toLearn);

    }

    /**
     * inject marked questions to actualQuestionsList
     */
    public void injectMarked() {
        actualQuestionsList=getQuestionsOfMarked(powtorzenia.getActualRepetitions());
    }

    private List<Pytanie> getPytaniaOfRepetitions(List<Powtorzenie> toLearn) {

        List<Pytanie>result=new ArrayList<>();
        for(Powtorzenie temp:toLearn)
        {
           result.addAll(getPytaniaPowtorzenia(temp));

        }
        return result;
    }

    /**
     * get questions from marked repetitions, adds all of them
     * to first repetition an then remove the rest of marked repetitions
     * @param toLearn
     */
    public void collectMarked(List<Powtorzenie> toLearn) {

        if (toLearn==null || toLearn.isEmpty()) {
            return;
        }
        toLearn=toLearn.stream().filter(Powtorzenie::isProblems).collect(Collectors.toList());
        List<Pytanie> questions = getPytaniaOfRepetitions(toLearn);
       Powtorzenie firstRepetition=toLearn.get(0);
       addPytaniaToPowtorzenie(firstRepetition,questions);

       toLearn.remove(0);
       powtorzenia.dropRepetitions(toLearn);

    }

    public List<Pytanie> getQuestionsByTagName(String tags, List<Pytanie> actualQuestionsList) {

        final List<String>listOfTagsNames=tagService.slicingForTagNames(tags);
      return   actualQuestionsList.stream().filter(a->{
            String tagsNames=tagService.getQuestionTagsNames(a);
            for (final String tag:listOfTagsNames)
            {
                if(tagsNames.indexOf(tag)!=-1)
                {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());

    }

    public List<Pytanie> getActualQuestionsList() {
        return actualQuestionsList;
    }

    /**
     * eliminate questions without problems from actualQuestionsList
     */
    public void retainProblems() {
        actualQuestionsList=actualQuestionsList.stream().filter(a->a.isProblem()).collect(Collectors.toList());
    }


    public void setActualQuestionsList(List<Pytanie> newActualQuestionsList) {

        actualQuestionsList=newActualQuestionsList;
    }

    public void addQuestionsList(Powtorzenie akutalnePowtorzenie, String questionsList) {
        StringTokenizer list=new StringTokenizer(questionsList, "\n");
        ArrayList<String>elementsList=new ArrayList<>();
        while (list.hasMoreTokens())
        {
            elementsList.add(list.nextToken());
        }

        elementsList.stream().forEach((String element)->{
            String[]questionAndAnswer=element.split(",");
            Pytanie newQuestion=new Pytanie(akutalnePowtorzenie,questionAndAnswer[1],questionAndAnswer[0]);
            pytania.createPytanie(newQuestion);
                });

    }
}
