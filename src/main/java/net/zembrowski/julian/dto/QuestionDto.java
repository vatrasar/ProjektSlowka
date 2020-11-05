package net.zembrowski.julian.dto;

import net.zembrowski.julian.domain.Pytanie;

public class QuestionDto {
    String question;
    String answer;
    int id;

    public QuestionDto(Pytanie question) {
       answer=question.getAnswer();
       this.question=question.getQuestion();
       this.id=question.getId();

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
}
