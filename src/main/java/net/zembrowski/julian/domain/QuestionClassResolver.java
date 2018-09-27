package net.zembrowski.julian.domain;

public enum QuestionClassResolver implements HtmlClassResolver {

    veryEasy("veryEasyQ",0,2),easy("easyQ",2,6),medium("mediumQ",6,11),hard("hardQ",11,21),dark("darkQ",21,1000);

    private String name;
    private int min,max;

    QuestionClassResolver(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }


    @Override
    public boolean IsNumberInRange(int number)
    {
        return (number>=min && number<max);
    }

    @Override
    public String resolveClass(int repetesNumber)
    {
        for (HtmlClassResolver spr : QuestionClassResolver.values())
        {
            if (spr.IsNumberInRange(repetesNumber))
            {
                return spr.getName();
            }
        }
        return dark.getName();
    }

    @Override
    public String getName() {
        return name;
    }
}
