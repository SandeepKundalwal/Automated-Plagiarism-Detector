package in.ac.iitmandi;

import in.ac.iitmandi.service.EvalService;
import in.ac.iitmandi.transferFiles.TransferFiles;
import in.ac.iitmandi.unzip.Unzip;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Plag Detection Utility.");

        EvalService evalService = new EvalService();
        Unzip unzip = new Unzip();
        TransferFiles transferFiles = new TransferFiles();

        /*
        * zipFilePath - where the zip folders are present
        * destDir - destination directory where the unzipped files will be moved
        * finalDest - destination where the files inside the unzipped folder will be moved with proper naming convention i.e <rollno.>_<problemno.>
        */
        String zipFilePath = "";
        String destDir = "";
        String finalDest = "";

        unzip.unzipAllFiles(zipFilePath, destDir);
        Map<String, List<File>> asgFileMap = transferFiles.getAllAssignmentFiles(destDir);
        evalService.saveAllAssignmentFiles(asgFileMap, finalDest);
        for(String key : asgFileMap.keySet()) {
            System.out.println(key + " : " + asgFileMap.get(key).size());
        }
    }
}
