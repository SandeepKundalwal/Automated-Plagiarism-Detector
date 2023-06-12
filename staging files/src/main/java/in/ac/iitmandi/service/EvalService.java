package in.ac.iitmandi.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class EvalService {

    public static final String SLASH = "\\";
    public static final String UNDERSCORE = "_";
    public void saveAllAssignmentFiles(Map<String, List<File>> assignments, String finalDest) {
        /**
         * This part should be dynamic depending on the number of questions.
         */
        String ques1 = "ques_1";
        String ques2a = "ques_2a";
        String ques2b = "ques_2b";
        String ques2c = "ques_2c";
        String ques3a = "ques_3a";
        String ques3b = "ques_3b";
        String ques3c = "ques_3c";
        String misc = "misc";
        for(String key : assignments.keySet()) {
            List<File> files = assignments.get(key);
            for(File file : files) {
                try {
                    StringBuilder dest = new StringBuilder(finalDest);
                    if(file.getName().contains("problem1")) {
                        dest.append(ques1);
                    } else if(file.getName().contains("problem2a")) {
                        dest.append(ques2a);
                    } else if(file.getName().contains("problem2b")) {
                        dest.append(ques2b);
                    } else if(file.getName().contains("problem2c")) {
                        dest.append(ques2c);
                    } else if(file.getName().contains("problem3a")) {
                        dest.append(ques3a);
                    } else if(file.getName().contains("problem3b")) {
                        dest.append(ques3b);
                    } else if(file.getName().contains("problem3c")) {
                        dest.append(ques3c);
                    } else {
                        dest.append(misc);
                    }
                    dest.append(SLASH).append(key).append(UNDERSCORE).append(file.getName());
                    Files.move(Paths.get(file.getPath()), Paths.get(dest.toString()), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
