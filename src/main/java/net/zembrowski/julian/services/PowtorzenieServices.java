package net.zembrowski.julian.services;


import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.repository.PowotrzenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PowtorzenieServices {

    @Autowired
    private PowotrzenieRepository powtorzenia;

    @Autowired
    private Powtorzenie aktualnePowtorzenie;

    public boolean isExist(Powtorzenie nowePowtorzenie) {
        return  powtorzenia.isExist(nowePowtorzenie);
    }

    @Transactional
    public void persistPowtorzenie(Powtorzenie nowePowtorzenie) {
        powtorzenia.persistPowtorzenie(nowePowtorzenie);
    }
    public void ustawJakoAktualne(Powtorzenie noweAktualne)
    {
        aktualnePowtorzenie.setDzien(noweAktualne.getDzien());
        aktualnePowtorzenie.setNumer(noweAktualne.getNumer());
        aktualnePowtorzenie.setNazwa(noweAktualne.getNazwa());
        aktualnePowtorzenie.setWlasciciel(noweAktualne.getWlasciciel());
        aktualnePowtorzenie.setNastepne(noweAktualne.getNastepne());
        aktualnePowtorzenie.setUtworzenie(noweAktualne.getUtworzenie());
    }

    public List<Powtorzenie> getPowtorzeniaNaDzis() {

        LocalDate dzis=LocalDate.now();
        return powtorzenia.getPowtorzeniaNaDzis(dzis);
    }

    public Powtorzenie getPowtorzenie(Klucz klucz) {
       return powtorzenia.getPowtorzenie(klucz);
    }

    /*zwroci maxymalny numer powtorzenia o danej nazwie*/
    public int getMaxNumer(String nazwa) {

        return powtorzenia.getMaxNumer(nazwa);
    }


}
