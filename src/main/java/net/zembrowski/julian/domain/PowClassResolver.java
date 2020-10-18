package net.zembrowski.julian.domain;

public enum PowClassResolver implements HtmlClassResolver {

    veryEasy("veryEasy",0,20),easy("easy",20,50),medium("medium",50,100),hard("hard",100,150),dark("dark",150,5000);

    private String name;
    private int min,max;

    PowClassResolver(String name, int min, int max) {
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
        for (HtmlClassResolver spr : PowClassResolver.values())
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
