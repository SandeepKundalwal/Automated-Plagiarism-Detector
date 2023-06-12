package in.ac.iitmandi.unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {

    public void unzipAllFiles(String zipFilePath, String destDir) {
        try {
            try (Stream<Path> paths = Files.walk(Paths.get(zipFilePath))) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            String fileName = path.toFile().getName();
                            if(fileName.toLowerCase().contains("b22")) {
                                int index = fileName.toLowerCase().indexOf("b22");
                                String rollNum = fileName.substring(index, index+6).toLowerCase();
                                unzip(path.toString(), destDir, rollNum);
                            } else {
                                System.out.println("Evaluate Manually : " + fileName);
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Will unzip each zip folder
     * @param zipFilePath
     * @param destDir
     * @param fileNamePrefix - RollNo. of the student who uploaded the zip file
     */
    public void unzip(String zipFilePath, String destDir, String fileNamePrefix) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()){
            dir.mkdirs();
        }
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
//                System.out.println(fileName);
                if(fileName.contains(".py") && !fileName.contains("samplescript.py")) {
                    File newFile = new File(destDir + File.separator + fileNamePrefix + "_" + fileName);
//                    System.out.println("Unzipping to "+newFile.getAbsolutePath());
                    //create directories for subdirectories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                } else {
//                    System.out.println("Extract manually : " + fileName);
                }
//                close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}