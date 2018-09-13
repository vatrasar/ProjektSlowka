package net.zembrowski.julian.domain;


import javafx.scene.shape.Path;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
public class MediaSource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   private int id;
    @ManyToOne
   private Pytanie pytanie;

    String path;
    //is it for answer or question?
    private MediaStatus status;

    public MediaSource() {

    }

    public MediaSource(String path ,Pytanie pytanie, MediaStatus status) {

        this.pytanie = pytanie;
        this.status = status;
        this.path=path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pytanie getPytanie() {
        return pytanie;
    }

    public void setPytanie(Pytanie pytanie) {
        this.pytanie = pytanie;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }
}
