package net.zembrowski.julian.services;


import net.zembrowski.julian.domain.Role;
import net.zembrowski.julian.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("session")
public class RoleServices {

    @Autowired
    RoleRepository role;

    @Transactional
    public void persistRole(String user,String nazwaRoli){
        role.persistRole(new Role(user,nazwaRoli));

    }

}
