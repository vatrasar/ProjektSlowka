package net.zembrowski.julian.repository;

import net.zembrowski.julian.domain.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope("session")
public class RoleRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void persistRole(Role role) {
        em.persist(role);
    }

}
