package net.zembrowski.julian;

import net.zembrowski.julian.domain.Powtorzenie;
import net.zembrowski.julian.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class Starter implements CommandLineRunner {

    EntityManager em;
    @Autowired
    RoleRepository roleRepository;
    @Override
    @Transactional
    public void run(String... args) throws Exception {




    }
}
