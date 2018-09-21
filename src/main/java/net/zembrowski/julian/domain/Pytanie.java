package net.zembrowski.julian.domain;



import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Entity
@Component
public class Pytanie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private   int id;
    private String question;
    @Column(nullable = true,columnDefinition = "varchar(2000)")
    private  String answer;
    @ManyToOne
   private Powtorzenie powtorzenie;
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Powtorzenie getPowtorzenie() {
        return powtorzenie;
    }

    public void setPowtorzenie(Powtorzenie powtorzenie) {
        this.powtorzenie = powtorzenie;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPytanie(Pytanie nowe) {
        this.question = nowe.question;
        this.answer = nowe.answer;
        this.powtorzenie = nowe.powtorzenie;
        this.status = nowe.status;
        this.id=nowe.id;
    }


    @Override
    public String toString() {
        return "Pytanie{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", powtorzenie=" + powtorzenie +
                ", status=" + status +
                '}';
    }

    public Pytanie() {

    }

    public Pytanie(Pytanie nowe)
    {
        id=nowe.getId();
        powtorzenie=nowe.getPowtorzenie();
        answer=nowe.getAnswer();
        question=nowe.getQuestion();
        status=nowe.getStatus();
    }

    /**
     * swap question and answer
     */
    public Pytanie reverse() {
        String temp=answer;
        answer=question;
        question=temp;

        return this;
    }
}
