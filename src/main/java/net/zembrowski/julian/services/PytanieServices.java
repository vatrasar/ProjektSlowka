package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.repository.PytanieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

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
    private static String path="C:\\Users\\Vatrasar\\Dysk Google\\programDane";//path to folder with video and img data

    @Transactional
    public void createPytanie(Pytanie nowePytanie, MultipartFile[] plikiAns, MultipartFile[] plikiOdp)
    {
        pytania.createPytanie(nowePytanie);

        saveFiles(plikiAns, MediaStatus.ANSWER,nowePytanie);
        saveFiles(plikiOdp, MediaStatus.QUESTION, nowePytanie);
    }

    /**
     * saev files to directory and their paths to database
     * @param pliki
     * @param status
     * @param nowePytanie
     */
    private void saveFiles(MultipartFile[] pliki, MediaStatus status, Pytanie nowePytanie) {
        for (MultipartFile plik:pliki)
        {
            Path sciezka= Paths.get(path,plik.getOriginalFilename());
            try {
                Files.write(sciezka,plik.getBytes());

                mediaSourceService.persistanceMediaSource(new MediaSource(sciezka.toString(),nowePytanie,status));
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

    public List<Pytanie> getPytaniaPowtorzeniaNiesprawdzone(Powtorzenie wykonywane) {

       return pytania.getPytaniaPowtorzeniaNiesprawdzone(wykonywane);


    }

    @Transactional
    public void zatwierdzWykonaniePowtorzenia(Powtorzenie wykonywane) {

        List<Pytanie>bledy=pytania.getBledy(wykonywane);

        if(bledy.size()!=0) {
            //uzyskanie numeru jakie powinno miec powtorzenie z bledami
            int numer = 1 + powtorzenia.getMaxNumer(wykonywane.getNazwa());

            //tworzymy nowe powtorzenie dla bledów
            Powtorzenie powDlaBledow = new Powtorzenie();
            powDlaBledow.utworzPowDlaBledow(wykonywane, numer);

            //dodanie nowego powtrzenia do bazy
            powtorzenia.persistPowtorzenie(powDlaBledow);
            //dodaj bledy do danego powtorzenia
            for (Pytanie modyfikowane : bledy) {
                pytania.dodajDoPowtorzenia(modyfikowane, powDlaBledow);

            }
            pytania.nadajStatusNiesprawdzone(powDlaBledow);

        }
        wykonywane.refaktoryzujPowtorzenie();
        powtorzenia.updatePowtorzenie(wykonywane);
        pytania.nadajStatusNiesprawdzone(wykonywane);


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
            powDlaBledow.utworzPowDlaBledow(wykonywane, numer);

            powDlaBledow.setEmpty(true);
            //dodanie nowego powtrzenia do bazy
            powtorzenia.persistPowtorzenie(powDlaBledow);

        }
        wykonywane.refaktoryzujPowtorzenie();
        powtorzenia.updatePowtorzenie(wykonywane);

    }



    @Transactional
    public void zmienStatusPytania(Integer id, Status status) {
        pytania.zmienStatusPytania(id,status);
    }

    @Transactional
    public void deletePytanie(int id) {
        pytania.deletePytanie(id);
    }

    @Transactional
    public void editPytanie(Pytanie edytowane) {

        Pytanie stare=pytania.getPytanie(edytowane.getId());
        edytowane.setPowtorzenie(stare.getPowtorzenie());
        Pytanie nowe=new Pytanie();
        nowe.setPowtorzenie(edytowane.getPowtorzenie());
        nowe.setQuestion(edytowane.getQuestion());
        nowe.setAnswer(edytowane.getAnswer());
        pytania.deletePytanie(edytowane.getId());
        pytania.createPytanie(nowe);
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
    public Status determineStatus(Integer zal, Pytanie aktualne) {


        if (zal==0)
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
}
