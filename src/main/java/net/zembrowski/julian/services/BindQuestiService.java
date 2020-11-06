package net.zembrowski.julian.services;

import net.zembrowski.julian.domain.BindQuestions;
import net.zembrowski.julian.repository.BindQuestionsRepository;
import net.zembrowski.julian.repository.PowotrzenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("session")
public class BindQuestiService {

    @Autowired
    private BindQuestionsRepository bindQuestionsRepository;

    public void bindQuestions(int id, int targetquestionId) {
        if(id!=targetquestionId)
            bindQuestionsRepository.addBind(new BindQuestions(id,targetquestionId));
    }

    public void dropBind(int id, int id1) {
        bindQuestionsRepository.dropBind(id,id1);
    }
}
