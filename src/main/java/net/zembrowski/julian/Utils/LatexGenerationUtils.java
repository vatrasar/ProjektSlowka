package net.zembrowski.julian.Utils;

import java.io.*;

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
    public static void copyFile(String source) {
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            File afile =new File(source.substring(1,source.length()));
            File bfile =new File("latexProject/zdjecia/"+afile.getName());

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0){

                outStream.write(buffer, 0, length);

            }


            inStream.close();
            outStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
