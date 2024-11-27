package com.ruufilms.services.zip;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipperSplitter {
    private String sourceFilePath;
    private String splitterFilePathPrefix;
    private int partSizeMB = 4000; // Default splitter size: 4000 MB

    public int getPartSizeMB() {
        return partSizeMB;
    }

    public void setPartSizeMB(int partSizeMB) {
        this.partSizeMB = partSizeMB;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getSplitterFilePathPrefix() {
        return splitterFilePathPrefix;
    }


    public void setSplitterFilePathPrefix(String splitterFilePathPrefix) {
        this.splitterFilePathPrefix = splitterFilePathPrefix;
    }

    public FileZipperSplitter(){

    }

    public FileZipperSplitter(String sourceFilePath, String splitterFilePathPrefix) {
        this.sourceFilePath = sourceFilePath;
        this.splitterFilePathPrefix = splitterFilePathPrefix;
    }

    public FileZipperSplitter(String sourceFilePath, String splitterFilePathPrefix, int partSizeMB) {
        this.sourceFilePath = sourceFilePath;
        this.splitterFilePathPrefix = splitterFilePathPrefix;
        this.partSizeMB = partSizeMB;
    }

    public ArrayList<String> process() {
        ArrayList<String> splitFilePaths = new ArrayList<>();
        try {
            // Generate the zip file name based on the input file name
            File sourceFile = new File(getSourceFilePath());
            String zipFilePath = sourceFile.getParent() + "/" + sourceFile.getName() + ".zip";

            // Zip the file
            zipFile(sourceFilePath, zipFilePath);

            // Split the zipped file and collect split file paths
            splitFilePaths = splitFile(zipFilePath, getSplitterFilePathPrefix(), getPartSizeMB());

            // Delete the zip file
            deleteFile(zipFilePath);

            // Optionally delete the original file
            deleteFile(getSourceFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return splitFilePaths;
    }

    private static void zipFile(String sourceFilePath, String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFilePath)) {

            ZipEntry zipEntry = new ZipEntry(new File(sourceFilePath).getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            System.out.println("File zipped successfully: " + zipFilePath);
        }
    }

    private static ArrayList<String> splitFile(String filePath, String splitFilePathPrefix, int partSizeMB) throws IOException {
        File file = new File(filePath);
        String fileNameWithoutExtension = file.getName();
        ArrayList<String> splitFilePaths = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024 * 1024 * partSizeMB];
            int partNumber = 1;
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) > 0) {
                String partFileName = splitFilePathPrefix + fileNameWithoutExtension + String.format(".%03d", partNumber);
                splitFilePaths.add(partFileName);

                try (FileOutputStream fos = new FileOutputStream(partFileName)) {
                    fos.write(buffer, 0, bytesRead);
                    System.out.println("Created: " + partFileName);
                }
                partNumber++;
            }
        }

        return splitFilePaths;
    }

    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            System.out.println("Deleted: " + filePath);
        } else {
            System.out.println("Failed to delete: " + filePath);
        }
    }
}
