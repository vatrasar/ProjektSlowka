package net.zembrowski.julian.domain;

public enum HtmlClassResolver {

    veryEasy("veryEasy",0,10),easy("easy",10,20),medium("medium",20,30),hard("hard",30,40),dark("dark",40,1000);

    private String name;
    private int min,max;

    HtmlClassResolver(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    private boolean IsNumberInRange(int number)
    {
        return (number>=min && number<max);
    }

    public static String resolveClass(int repetesNumber)
    {
        for (HtmlClassResolver spr : HtmlClassResolver.values())
        {
            if (spr.IsNumberInRange(repetesNumber))
            {
                return spr.getName();
            }
        }
        return dark.getName();
    }

    public String getName() {
        return name;
    }
}
