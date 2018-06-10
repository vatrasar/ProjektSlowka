package net.zembrowski.julian.services;


import net.zembrowski.julian.repository.PowotrzenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PowtorzenieServices {

    @Autowired
    PowotrzenieRepository powtorzenia;

}
