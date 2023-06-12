package in.ac.iitmandi.transferFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferFiles {

    public Map<String, List<File>> getAllAssignmentFiles(String directoryName) {
        Map<String, List<File>> res = new HashMap<>();
        File directory = new File(directoryName);
        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null) {
            for (File dir : fList) {
                String[] fileNameSplit = dir.getName().split("_");
                if(fileNameSplit.length > 1 && dir.isDirectory()) {
                    String rollNumber = fileNameSplit[0];
                    List<File> asgFileList = getAssignmentFiles(dir, new ArrayList<>());
                    if(asgFileList != null && !asgFileList.isEmpty()) {
                        res.put(rollNumber, asgFileList);
                    }
                }
            }
        }
        return res;
    }

    public List<File> getAssignmentFiles(File dir, List<File> assignmentList) {
        boolean containsFile = false;
        File[] fList = dir.listFiles();
        for (File file : fList) {
            if (file.isFile() && file.getName().contains("py")) {
                containsFile = true;
                assignmentList.add(file);
            } else if (file.isDirectory() && !containsFile) {
                getAssignmentFiles(file, assignmentList);
            }
        }
        return assignmentList;
    }
}
