package iitmandi.ac.in;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Services {
    public static final String outputFilePath = "C:\\Users\\DELL\\Desktop\\TA Duty\\IC 152\\Plagiarism Evaluation\\Assignment-7\\Plagiarism Report\\PlagiarismSummary.txt";
    public static final String DATA_SEPARATOR  = "------->";
    public static final String SPACE  = " ";

    public void getPlagiarismInfoAll() throws IOException {
        Map<String, TreeSet<String>> plagiarismDetected = new TreeMap<>();

        /**
         * Traverse the directory and get the absolute paths of all the HTML files and store them in a list, so that
         * we can traverse each individual file and extract the plagiarism percentage
         * @directory - is the directory path from where all the HTML reports will be fetched
         * @lambda expression -  to walk the directory and add the absolute path of each HTML file into the list
         */
        List<String> listFiles = new ArrayList<>();
        /**
         * directory where all the plagiarism reports are present
         */
        Path directory = Paths.get("C:\\Users\\DELL\\Desktop\\TA Duty\\IC 152\\Plagiarism Evaluation\\Assignment-7\\Plagiarism Report\\");
        Files.walk(directory).forEach(path -> listFiles.add(traverseDirectory(path.toFile())));

        /**
         * Traverse each HTML file and performing the web scrapping to extract the roll no. and plagiarism percentage and then
         * adding this information to the MAP.
         */
        for(String htmlFile : listFiles){
            if(htmlFile != null && htmlFile.contains(".html")){
                //extracting the question no.
                String questionNo = htmlFile.substring(htmlFile.lastIndexOf("_") + 1, htmlFile.lastIndexOf("."));
                //reading HTML File using JSoup
                Document htmlDocument = readHTMLFile(htmlFile);
                getPlagiarismInfo(questionNo, htmlDocument, plagiarismDetected);
                System.out.println(htmlFile);
            }
        }
        writeMapToFile(plagiarismDetected);
    }

    /**
     * Traverse the directory and get the absolute paths of all the files that are present in the directory
     * @param file - the name of the file
     * @return
     */
    public static String traverseDirectory(File file) {
        return (!file.isDirectory() ? file.getAbsolutePath() : null);
    }


    /**
     * Reading the HTML file using JSOUP
     * @param htmlFilePath
     * @return - HTML document
     */
    public Document readHTMLFile(String htmlFilePath){
        Document htmlFile = null;
        try{
            htmlFile = Jsoup.parse(new File(htmlFilePath), "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        return htmlFile;
    }


    /**
     * Scrapping the HTML File to get the Roll No. of those students who have plagiarised more than 90%(can be changed)
     * @param questionNo - checking plagiarism for which  question no.
     * @param htmlDocument - the HTML Document which contains the plagiarism report
     * @param plagiarismDetected - TREEMAP ------> key -> Roll No.
     *                                             value -> Question No. in which the student have plagiarised
     */
    public void getPlagiarismInfo(String questionNo, Document htmlDocument, Map<String, TreeSet<String>> plagiarismDetected) {
        //grabbing the <tr> tag which has <p> tag inside it.
        final Elements trElements = htmlDocument.select("tr:has(p)");

        for(Element trElement : trElements){
            String[] firstStudent = trElement.select("i").get(0).text().toLowerCase().split("/");
            String firstStudentRollNo = firstStudent[firstStudent.length - 1].toLowerCase().split("_")[0];
            Float copyPercentageFirstStudent = Float.parseFloat(trElement.select("b").get(0).text().split("%")[0]);

            String[] secondStudent = trElement.select("i").get(1).text().toLowerCase().split("/");
            String secondStudentRollNo = secondStudent[secondStudent.length - 1].toLowerCase().split("_")[0];
            Float copyPercentageSecondStudent = Float.parseFloat(trElement.select("b").get(1).text().split("%")[0]);

            /**
             * Determine the percentage above which the assignment will be considered as plagiarised
             */
            if((copyPercentageFirstStudent >= 90.00 && copyPercentageSecondStudent >= 90.00) && !firstStudentRollNo.equals(secondStudentRollNo)){
                /**
                 * Inserting the record of referencing student into MAP
                 */
                if(plagiarismDetected.containsKey(firstStudentRollNo)){
                    plagiarismDetected.get(firstStudentRollNo).add(questionNo);
                } else {
                    TreeSet<String> questionSet = new TreeSet<>(Collections.singleton(questionNo));
                    plagiarismDetected.put(firstStudentRollNo, questionSet);
                }

                /**
                 * Inserting the record of referenced student into MAP
                 */
                if(plagiarismDetected.containsKey(secondStudentRollNo)){
                    plagiarismDetected.get(secondStudentRollNo).add(questionNo);
                } else {
                    TreeSet<String> questionSet = new TreeSet<>(Collections.singleton(questionNo));
                    plagiarismDetected.put(secondStudentRollNo, questionSet);
                }
            }
        }
    }

    /**
     * Generating a .txt file with the Roll No. that are caught for plagiarism
     * @param plagiarismDetected
     * @throws IOException
     */
    public void writeMapToFile(Map<String, TreeSet<String>> plagiarismDetected) throws IOException {
        BufferedWriter bufferWriter = null;
        try{
            bufferWriter = new BufferedWriter(new FileWriter(outputFilePath));
            bufferWriter.write("Number of students who copied " + DATA_SEPARATOR + SPACE + plagiarismDetected.size());
            bufferWriter.newLine();
            bufferWriter.newLine();
            bufferWriter.write("#################################################################################################################################");
            bufferWriter.newLine();
            bufferWriter.newLine();
            for(Map.Entry<String, TreeSet<String>> entry : plagiarismDetected.entrySet()){
                bufferWriter.write(entry.getKey() + SPACE + DATA_SEPARATOR + SPACE + entry.getValue());
                bufferWriter.newLine();
            }
            bufferWriter.newLine();
            bufferWriter.write("#################################################################################################################################");
            bufferWriter.newLine();
            bufferWriter.flush();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                bufferWriter.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
