package net.zembrowski.julian.domain;


/**
 * class resolver for PokazDPocwiczenia
 */
public class HtmlClassResolverExercises{



    public String resolveClass(Powtorzenie repetition,int repetitionSize,boolean hasProblems,Powtorzenie last) {
        String name=QuestionClassResolver.dark.resolveClass(repetitionSize);
        if(hasProblems)
        {
            name="hasProblem";
        }
        if(repetition.isProblems())
        {
            if(name.equals("hasProblem"))
                name=name+"problem";
            else
                name="problem";
        }
        if(repetition.equals(last))
        {
            if(name.contains("problem"))
                name="lastProblem";
            else
                name="last";
        }
        return name;
    }

    public String getTextForMarked(Powtorzenie repetition)
    {
        if (repetition.isProblems())
        {
            return "odznacz";
        }
        else
            return "zaznacz";
    }

}


































