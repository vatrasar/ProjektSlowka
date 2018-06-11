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
        if (bledy.isEmpty())
        {
            wykonywane.refaktoryzujPowtorzenie();

        }
        else
        {
            //uzyskanie numeru jakie powinno miec powtorzenie z bledami
            int numer=1+powtorzenia.getMaxNumer(wykonywane.getNazwa());

            //tworzymy nowe powtorzenie dla bledów
            Powtorzenie powDlaBledow=new Powtorzenie();
            powDlaBledow.utworzPowDlaBledow(wykonywane,numer);

            //dodanie nowego powtrzenia do bazy
            powtorzenia.persistPowtorzenie(powDlaBledow);
            //dodaj bledy do danego powtorzenia
            for (Pytanie modyfikowane:bledy)
            {
                pytania.dodajDoPowtorzenia(modyfikowane,powDlaBledow);

            }
            pytania.nadajStatusNiesprawdzone(powDlaBledow);
        }

        wykonywane.refaktoryzujPowtorzenie();
        pytania.nadajStatusNiesprawdzone(wykonywane);


    }

    @Transactional
    public void zmienStatusPytania(Integer id, Status status) {
        pytania.zmienStatusPytania(id,status);
    }
}
