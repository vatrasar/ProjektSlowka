package net.zembrowski.julian;

import net.zembrowski.julian.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Starter implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {


    }
}
