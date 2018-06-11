package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.Pytanie;
import net.zembrowski.julian.repository.PytanieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PytanieServices {

    @Autowired
    PytanieRepository pytania;

    @Transactional
    public void createPytanie(Pytanie nowePytanie)
    {
        pytania.createPytanie(nowePytanie);
    }
}
