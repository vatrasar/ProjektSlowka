package net.zembrowski.julian.services;


import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.repository.PowotrzenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PowtorzenieServices {

    @Autowired
    PowotrzenieRepository powtorzenia;

    @Autowired
    Powtorzenie aktualnePowtorzenie;

    public boolean isExist(Powtorzenie nowePowtorzenie) {
        return  powtorzenia.isExist(nowePowtorzenie);
    }

    @Transactional
    public void persistPowtorzenie(Powtorzenie nowePowtorzenie) {
        powtorzenia.persistPowtorzenie(nowePowtorzenie);
    }
    public void ustawJakoAktualne(Powtorzenie noweAktualne)
    {
        aktualnePowtorzenie.setDzienPowtorzenia(noweAktualne.getDzienPowtorzenia());
        aktualnePowtorzenie.setNumer(noweAktualne.getNumer());
        aktualnePowtorzenie.setNazwa(noweAktualne.getNazwa());
        aktualnePowtorzenie.setWlasciciel(noweAktualne.getWlasciciel());
        aktualnePowtorzenie.setNastepne(noweAktualne.getNastepne());
        aktualnePowtorzenie.setUtworzenie(noweAktualne.getUtworzenie());
    }
}
