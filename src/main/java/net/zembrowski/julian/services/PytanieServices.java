package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.domain.Status;
import net.zembrowski.julian.repository.PytanieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PytanieServices {

    @Autowired
    private PytanieRepository pytania;
    @Autowired
    private PowtorzenieServices powtorzenia;

    @Transactional
    public void createPytanie(Pytanie nowePytanie)
    {
        pytania.createPytanie(nowePytanie);
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
}
