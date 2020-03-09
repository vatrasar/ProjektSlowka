package net.zembrowski.julian.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LatexGenerationUtils {


    public static final String SECTION="section";
    public static final String SUBSECTION="subsection";
    public static final String SUBSUBSECTION="subsubsection";
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

    /**
     * replace polish characters for allowed in listing
     * @param textToMap
     * @return
     */
    public static String mapPolishCharacters(String textToMap)
    {
        String result=textToMap.replaceAll("ą","a");
        result=result.replaceAll("ź|ż","z");
        result=result.replaceAll("ł","l");
        result=result.replaceAll("ó","o");
        return result.replaceAll("ć","c");

    }

    public static void copyFile(String source) {
        InputStream inStream = null;
        OutputStream outStream = null;
        while(true) {

            try {
                File afile = new File(source.substring(1, source.length()));
                File bfile = new File("latexProject/zdjecia/" + afile.getName());

                inStream = new FileInputStream(afile);
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
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
