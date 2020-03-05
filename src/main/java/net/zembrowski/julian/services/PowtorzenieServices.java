package net.zembrowski.julian.services;


import net.zembrowski.julian.domain.*;
import net.zembrowski.julian.repository.PowotrzenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static net.zembrowski.julian.domain.Status.NIESPRAWDZONE;
import static net.zembrowski.julian.domain.Status.UMIEM_JEDNA_STRONE;

@Service
@Scope("session")
public class PowtorzenieServices {

    @Autowired
    private PowotrzenieRepository powtorzenia;

    @Autowired
    private Powtorzenie aktualnePowtorzenie;

    @Autowired
    private TagService tagService;
    private List<Powtorzenie>toLearn;
    private Powtorzenie actualRepetition;
    public PowtorzenieServices() {
        this.toLearn = null;
        this.actualRepetition=null;
    }

    public Powtorzenie getActualRepetition() {
        return actualRepetition;
    }

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
        aktualnePowtorzenie.setHard(noweAktualne.isHard());

    }

    public List<Powtorzenie> getPowtorzeniaNaDzis() {

        LocalDate dzis=LocalDate.now();
        return powtorzenia.getPowtorzeniaNaDzis(dzis);
    }

    public Powtorzenie getAktualnePowtorzenie() {
        return aktualnePowtorzenie;
    }

    public Powtorzenie getPowtorzenie(Klucz klucz) {

       return powtorzenia.getPowtorzenie(klucz);
    }

    /*zwroci maxymalny numer powtorzenia o danej nazwie*/
    public int getMaxNumer(String nazwa) {

        return powtorzenia.getMaxNumer(nazwa);
    }


    private List<Powtorzenie> getRepetsForTomorrow() {
        LocalDate jutro=LocalDate.now().plusDays(1);
        List<Powtorzenie>naJutro=powtorzenia.getPowtorzeniaNaDzis(jutro);
       onlyAfterOneDayStays(naJutro);
        return naJutro;
    }

    /**
     * inject repetitions for tomorrow to toLearn pool
     */
    public void injectRepetitionsForTomorrow()
    {
        toLearn=getRepetsForTomorrow();
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

        tagService.dropTagsOfRepetition(powtorzenie);

        powtorzenia.remove(powtorzenie);

    }

    public List<Powtorzenie> getPowtorzeniaByName(String name) {
       return powtorzenia.getPowtorzeniaByName(name);
    }

    public List<Powtorzenie> getPowtorzeniaByDate(LocalDate repetitionDate) {


        return powtorzenia.getPowtorzeniaByDate(repetitionDate);
    }

    public List<Powtorzenie> getPowtorzeniaByTags(String tags) {


        return tagService.getTagsRepetitionList(tags);
    }

    @Transactional
    public void dropRepetitions(List<Powtorzenie> toDrop) {

        for(Powtorzenie temp:toDrop)
        {
            dropPowotrzenie(temp);
        }
    }

    /**
     * archives akutalnePowturzenie. Set next repetition for 1000_000 days from now
     * @param akutalnePowtorzenie
     */
    @Transactional
    public void archPow(Powtorzenie akutalnePowtorzenie) {
        akutalnePowtorzenie.setNastepne(1000_000);
        akutalnePowtorzenie.setDzien(akutalnePowtorzenie.getDzien().plusDays(1000_000));
        powtorzenia.updatePowtorzenie(akutalnePowtorzenie);
    }

    public List<Powtorzenie> getActualRepetitions() {
        return toLearn;
    }

    public void setToLearn(List<Powtorzenie> resultList) {
        toLearn=resultList;
    }

    public void injectActualRepetition(Powtorzenie processed) {
        actualRepetition=processed;
    }

    public int getQuestionsToEnd(List<Pytanie> actualQuestionsList) {
        int count=0;
        int clones=0;//one question can be more than one time in repetition
        for(Pytanie question : actualQuestionsList)
        {
            if(question.getPowtorzenie().isReverse())
            {
                if (question.getStatus()== NIESPRAWDZONE)
                {
                    List temp=actualQuestionsList.stream().filter((Pytanie a)->a.equals(question)).collect(Collectors.toList());
                    if(temp.size()>1)
                    {
                        clones++;
                    }
                    count+=2;

                }
                if (question.getStatus()== UMIEM_JEDNA_STRONE)
                {
                    count++;
                }
            }
            else{
                count=actualQuestionsList.size();
            }

        }
        if(clones!=0)
        {
            clones/=2;
            count-=clones;
        }
        return count;
    }

    public List<String> searchRepetitionsWithPartOfName(String partOfName) {
        return powtorzenia.getRepetitionsWithPartOfName(partOfName);

    }
}
