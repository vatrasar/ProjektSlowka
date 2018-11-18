package net.zembrowski.julian.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Statistics {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    int thirty;

    public Statistics(int id, int thirty) {
        this.id = id;
        this.thirty = thirty;
    }

    public Statistics() {
        thirty=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getThirty() {
        return thirty;
    }

    public void pushThirty() {

        thirty++;
    }

}
