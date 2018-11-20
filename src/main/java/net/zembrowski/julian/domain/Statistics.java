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
    private int thirty;
    private int ten;

    public Statistics(int id, int thirty,int ten) {
        this.id = id;
        this.thirty = thirty;
        this.ten=ten;
    }

    public Statistics() {
        thirty=0;
        ten=0;
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

    public int getTen() {
        return ten;
    }

    /**
     * add +1 to weight of appropriate statistic
     * @param next
     */
    public void pushStat(int next) {
        switch (next)
        {
            case 30:
                thirty++;
                break;
            case 10:
                ten++;
                break;
        }
    }
}
