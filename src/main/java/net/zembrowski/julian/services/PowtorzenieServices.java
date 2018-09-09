package net.zembrowski.julian.services;


import net.zembrowski.julian.domain.Klucz;
import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.repository.PowotrzenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Scope("session")
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
        aktualnePowtorzenie.setEmpty(noweAktualne.isEmpty());
        aktualnePowtorzenie.setReverse(noweAktualne.isReverse());
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


    public List<Powtorzenie> getRepetsToLearn() {
        LocalDate jutro=LocalDate.now().plusDays(1);
        List<Powtorzenie>naJutro=powtorzenia.getPowtorzeniaNaDzis(jutro);
       onlyAfterOneDayStays(naJutro);
        return naJutro;
    }

    private void onlyAfterOneDayStays(List<Powtorzenie>pow)
    {
        List<Powtorzenie>usuwane=new ArrayList<>();
        for(Powtorzenie sprawdzane: pow)
        {
            LocalDate jutro=LocalDate.now().plusDays(1);

            if (sprawdzane.getNastepne()!=1 || !sprawdzane.getDzien().isEqual(jutro))
            {
                usuwane.add(sprawdzane);
            }
        }
        pow.removeAll(usuwane);
    }

    @Transactional
    public void updatePowtorzenie(Powtorzenie wykonywane) {
        powtorzenia.updatePowtorzenie(wykonywane);
    }

    @Transactional
    public void remove(Powtorzenie powtorzenie) {
        powtorzenia.remove(powtorzenie);

    }

    public List<Powtorzenie> getPowtorzeniaNaDzien(LocalDate dzien) {

      return  powtorzenia.getPowtorzeniaNaDzien(dzien);
    }

    /**
     * sprawia że wartosc nastepne jest ustawiana na 1 a wartość dzien na nastepny dzień(czyli jutro)
     * @param powtorzenie
     */
    public void resetujPowtorzenie(Powtorzenie powtorzenie) {

        LocalDate dataNastepnego=LocalDate.now().plusDays(1);
        powtorzenie.setDzien(dataNastepnego);
        powtorzenie.setNastepne(1);
        powtorzenie.setProblems(false);
        powtorzenia.updatePowtorzenie(powtorzenie);


    }

    @Transactional
    public void setOpposedProblem(Klucz klucz) {

        Powtorzenie modifi=powtorzenia.getPowtorzenie(klucz);

        modifi.setProblems(!modifi.isProblems());
        powtorzenia.updatePowtorzenie(modifi);
    }

    /**
     * UWAGA: Pozostawi pytania powtórzenia nie usunięte!
     * @param powtorzenie
     */
    @Transactional
    public void dropPowotrzenie(Powtorzenie powtorzenie) {


        powtorzenia.remove(powtorzenie);

    }
}
