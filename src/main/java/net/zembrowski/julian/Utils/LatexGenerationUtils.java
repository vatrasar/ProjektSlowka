package net.zembrowski.julian.Utils;

public class LatexGenerationUtils {


    public static final String SECTION="section";
    public static final String SUBSECTION="subsection";
    public static final String TEXT="textit";
    public static final String DOCUMENT="document";
    /**
     *
     * @return
     * packInLatexTag("section", title) returns \section{title}
     */
    public static String packInLatexTag(String tagName, String target) {
        return String.format("\\%s{%s}",tagName,target);
    }
    public static String endl()
    {
        return "\n";
    }
    public static String packInBeginBlock(String blockName,String target)
    {
        return String.format("\n\\begin{%s}\n%s\n\\end{%s}\n",blockName,target,blockName);
    }

}
