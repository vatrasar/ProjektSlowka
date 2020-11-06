package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BindQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    int idQuestion1;
    int idQuestion2;

    public BindQuestions() {
    }

    public BindQuestions(int idQuestion1, int idQuestion2) {

        this.idQuestion1 = idQuestion1;
        this.idQuestion2 = idQuestion2;
    }
}
