package net.zembrowski.julian.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.logging.Logger;

public class LatexGenerationUtils {


    public static final String SECTION="section";
    public static final String SUBSECTION="subsection";
    public static final String SUBSUBSECTION="subsubsection";
    public static final String TEXT="textit";
    public static final String DOCUMENT="document";
    public static final String CHAPTER="chapter";
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

    /**
     * replace polish characters for allowed in listing
     * @param textToMap
     * @return
     */
    public static String mapPolishCharacters(String textToMap)
    {
        String result=textToMap.replaceAll("ą|Ą","a");
        result=result.replaceAll("ź|ż|Ż|Ź","z");
        result=result.replaceAll("ę|Ę","e");
        result=result.replaceAll("ł|Ł","l");
        result=result.replaceAll("ó|Ó","o");
        return result.replaceAll("ć","c");

    }

    public static void copyFile(String source) {
        InputStream inStream = null;
        OutputStream outStream = null;
        while(true) {

            try {
                File file = new File("latexProject/latexKonf.tex");
                Files.deleteIfExists(file.toPath());

                File afile = new File(source.substring(1, source.length()));
                File bfile = new File("latexProject/zdjecia/" + afile.getName());

                try {
                    inStream = new FileInputStream(afile);
                } catch (FileNotFoundException e) {
                    Logger.getGlobal().warning("no file with foto. No courrent data folder?");
                }
                outStream = new FileOutputStream(bfile);

                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0) {

                    outStream.write(buffer, 0, length);

                }


                inStream.close();
                outStream.close();
                break;

            } catch (IOException e) {

                try {
                    Files.createDirectories(Paths.get("latexProject/zdjecia"));

                    PrintStream writer=new PrintStream("latexProject/latexKonf.tex","UTF-8");
                    writer.print(getLatexKonfString());
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getLatexKonfString() {

        return "\\documentclass[a4paper,polish,12pt]{report}\n" +
                "    \\usepackage[utf8]{inputenc}\n" +
                "    \\usepackage[T1]{fontenc}\n" +
                "    \\usepackage{lmodern}\n" +
                "\t\\usepackage{amsmath}\n" +
                "\t\\usepackage{xcolor}\n" +
                "    \\usepackage{babel}\n" +
                "    \\usepackage{csquotes}\n" +
                "    \\DeclareQuoteAlias{croatian}{polish}\n" +
                "    \\usepackage[%\n" +
                "    style=verbose-ibid, % numeric, alphabetic, authoryear, ect.\n" +
                "    sorting=nty,\n" +
                "    isbn=false,\n" +
                "    abbreviate = false,\n" +
                "    backend=biber,]{biblatex}\n" +
                "\n" +
                "    \\renewcommand*{\\newunitpunct}{\\addcomma\\space}\n" +
                "    \\renewbibmacro*{in:}{}\n" +
                "\n" +
                "    \\usepackage{xpatch}\n" +
                "\n" +
                "    \\xpatchbibdriver{book}{%\n" +
                "    \\newunit\n" +
                "    \\iffieldundef{maintitle}\n" +
                "    {\\printfield{volume}%\n" +
                "    \\printfield{part}}\n" +
                "    {}%\n" +
                "    }\n" +
                "    {%\n" +
                "    }{}{}\n" +
                "\n" +
                "    \\xpatchbibdriver{book}{%\n" +
                "    \\usebibmacro{publisher+location+date}%\n" +
                "    }\n" +
                "    {%\n" +
                "    \\usebibmacro{publisher+location+date}%\n" +
                "    \\newunit\n" +
                "    \\printfield{volume}%\n" +
                "    \\printfield{part}\n" +
                "    \\usebibmacro{finentry}\n" +
                "    }{}{}\n" +
                "\n" +
                "    \\DeclareFieldFormat{journaltitle}{\\mkbibquote{#1}}\n" +
                "    \\DeclareFieldFormat[article,periodical]{number}{nr.  #1}% number of a journal\n" +
                "\n" +
                "    \\DeclareFieldFormat\n" +
                "    [article,inbook,incollection,inproceedings,patent,thesis,unpublished]\n" +
                "    {title}{\\mkbibemph{#1}}\n" +
                "    %\n" +
                "    \\renewbibmacro*{journal+issuetitle}{%\n" +
                "    \\usebibmacro{journal}%\n" +
                "    \\setunit*{\\addspace}%\n" +
                "    \\iffieldundef{series}\n" +
                "    {}\n" +
                "    {\\newunit\n" +
                "    \\printfield{series}%\n" +
                "    \\setunit{\\addspace}}%\n" +
                "    \\usebibmacro{issue+date}%\n" +
                "    \\setunit{\\addspace}%\n" +
                "    \\usebibmacro{issue}%\n" +
                "    \\setunit{\\addspace}%\n" +
                "    \\usebibmacro{volume+number+eid}%\n" +
                "    \\newunit}\n" +
                "\n" +
                "    \\renewbibmacro*{issue+date}{%\n" +
                "    \\iffieldundef{issue}\n" +
                "    {\\usebibmacro{date}}\n" +
                "    {\\printfield{issue}%\n" +
                "    \\setunit*{\\addspace}%\n" +
                "    \\usebibmacro{date}}%\n" +
                "    \\newunit}\n" +
                "\n" +
                "    \\renewbibmacro*{publisher+location+date}{%\n" +
                "    \\printlist{publisher}%\n" +
                "    \\iflistundef{publisher}\n" +
                "    {\\setunit*{\\addcomma\\space}}\n" +
                "    {\\setunit*{\\addcomma\\space}}%\n" +
                "    \\printlist{location}%\n" +
                "    \\setunit*{\\addspace}%\n" +
                "    \\usebibmacro{date}%\n" +
                "    \\newunit}\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage{color}\n" +
                "\n" +
                "\\usepackage{listings}\n" +
                " \\renewcommand{\\lstlistingname}{kod} %zmiana podpisu na polski\n" +
                "\n" +
                "\\definecolor{dkgreen}{rgb}{0,0.6,0}\n" +
                "\\definecolor{gray}{rgb}{0.5,0.5,0.5}\n" +
                "\\definecolor{mauve}{rgb}{0.58,0,0.82}\n" +
                "\n" +
                "\n" +
                "\\lstset{frame=tb,\n" +
                " language=Python,\n" +
                " aboveskip=3mm,\n" +
                " belowskip=3mm,\n" +
                " showstringspaces=false,\n" +
                " columns=flexible,\n" +
                " basicstyle={\\small\\ttfamily},\n" +
                " numbers=none,\n" +
                " numberstyle=\\tiny\\color{gray},\n" +
                " keywordstyle=\\color{blue},\n" +
                " commentstyle=\\color{dkgreen},\n" +
                " stringstyle=\\color{mauve},\n" +
                " breaklines=true,\n" +
                " breakatwhitespace=true,\n" +
                " tabsize=3\n" +
                "}\n" +
                "\n" +
                "\\usepackage{hyperref}\n" +
                "\\newtheorem{theorem}{Twierdzenie}\n" +
                "\\addbibresource{bik.bib}\n" +
                "\\newtheorem{tw}{Twierdzenie}\n" +
                "\\title{Projekt Cyfrowe przetwarzanie sygnałów}\n" +
                "\\date{}\n" +
                "\\author{Szymon Kozakiewicz}";

    }

    public static String packPairOfTags(String answer, String startTag, String endTag, String type) {
        int index=0;
        int startTagIndex=0;
        StringBuilder resultBulider = new StringBuilder();
        while(index<answer.length())
        {
            //search startTag
            if(isTagAtThatPosition(answer, startTag, index))
            {
                startTagIndex=index;
                //search for end tag
                while (index<answer.length())
                {

                    if(isTagAtThatPosition(answer,endTag,index))
                    {
                        index=index+endTag.length();
                        String textToPack=answer.substring(startTagIndex,index);
                        textToPack=textToPack.replace(startTag,"");
                        textToPack=textToPack.replace(endTag,"");
                        if(type.equals("list"))
                        {
                            StringBuilder listBulider=new StringBuilder();
                            //pack to list

                            String[] items=textToPack.split("\n");

                            for(String item:items)
                            {
                                if(item.length()>2) {
                                    listBulider.append("\\item ");
                                    listBulider.append(item);
                                    listBulider.append(endl());
                                }
                            }
                            resultBulider.append(packInBeginBlock("itemize",listBulider.toString())).append(endl());


                        }else if(type.equals("code"))
                        {
                            resultBulider.append(packInBeginBlock("lstlisting",textToPack)).append(endl());
                        }
                        break;

                    }

                    index++;
                }


            }
            else {

                resultBulider.append(answer.charAt(index));
            }
            index++;

        }
        return resultBulider.toString();
    }

    private static boolean isTagAtThatPosition(String text, String tagName, int index) {
//        boolean part1=text.charAt(index) == tagName.charAt(0);
//        if (part1)
//        {
//            boolean part2=index + tagName.length() - 1 < text.length();
//            String jeden=text.substring(index, index + tagName.length());
//
//            boolean part3=text.substring(index, index + tagName.length()).equals(tagName);
//            int a=2+2;
//        }
//


        return text.charAt(index) == tagName.charAt(0) && index + tagName.length()-1 < text.length() && text.substring(index, index + tagName.length()).equals(tagName);
    }
}
