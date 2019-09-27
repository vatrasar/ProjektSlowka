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
    //true when was problem witch question
    @Column(nullable = false,columnDefinition = "bit(1) default 0")
    private boolean problem;
    @Column(nullable = false,columnDefinition = "bit(1) default 0")
    boolean lastAdded;
    @Column(nullable = false,columnDefinition = "bit(1) default 0")
    boolean notion;
    @OneToOne
    Statistics statistics;

    public boolean isHard()
    {
        if(statistics.getThirty()>=1 || statistics.getTen()>=1)
            return true;
        else
            return false;
    }
    public Pytanie(Powtorzenie repetition) {
        powtorzenie=repetition;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public boolean isNotion() {
        return notion;
    }

    public void setNotion(boolean notion) {
        this.notion = notion;
    }

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

    public boolean isProblem() {
        return problem;
    }

    public void setProblem(boolean problem) {
        this.problem = problem;
    }

    public void setPytanie(Pytanie nowe) {
        this.question = nowe.question;
        this.answer = nowe.answer;
        this.powtorzenie = nowe.powtorzenie;
        this.status = nowe.status;
        this.id=nowe.id;
        this.problem=nowe.problem;
        this.notion=nowe.notion;
        this.statistics=nowe.statistics;
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
    public Pytanie(Powtorzenie repetiotion,String answer,String question)
    {
      notion=false;
      problem=false;
      powtorzenie=repetiotion;
      this.answer=answer;
      this.question=question;
    }
    public Pytanie(Pytanie nowe)
    {
        id=nowe.getId();
        powtorzenie=nowe.getPowtorzenie();
        answer=nowe.getAnswer();
        question=nowe.getQuestion();
        status=nowe.getStatus();
        problem=nowe.problem;
        this.notion=nowe.notion;
        this.statistics=nowe.statistics;
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

    public boolean isLastAdded() {
        return lastAdded;
    }

    public void setLastAdded(boolean lastAdded) {
        this.lastAdded = lastAdded;
    }

    public void pushStatistic(int next) {
        statistics.pushStat(next);


    }
}
