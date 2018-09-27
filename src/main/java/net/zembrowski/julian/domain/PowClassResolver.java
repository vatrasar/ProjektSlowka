package net.zembrowski.julian.domain;

public enum PowClassResolver implements HtmlClassResolver {

    veryEasy("veryEasy",0,10),easy("easy",10,20),medium("medium",20,30),hard("hard",30,40),dark("dark",40,1000);

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
