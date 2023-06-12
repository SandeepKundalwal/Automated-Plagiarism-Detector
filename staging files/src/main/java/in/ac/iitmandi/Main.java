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


        String zipFilePath = "C:\\Users\\DELL\\Desktop\\TA Duty\\IC 152\\Plagiarism Evaluation\\Assignment-8\\Assignment8";
        String destDir = "C:\\Users\\DELL\\Desktop\\TA Duty\\IC 152\\Plagiarism Evaluation\\Assignment-8\\Assignment8_Unzipped";
        String finalDest = "C:\\Users\\DELL\\Desktop\\TA Duty\\IC 152\\Plagiarism Evaluation\\Assignment-8\\Assignment8_Eval_Out\\";

        unzip.unzipAllFiles(zipFilePath, destDir);
        Map<String, List<File>> asgFileMap = transferFiles.getAllAssignmentFiles(destDir);
        evalService.saveAllAssignmentFiles(asgFileMap, finalDest);
        for(String key : asgFileMap.keySet()) {
            System.out.println(key + " : " + asgFileMap.get(key).size());
        }
    }
}