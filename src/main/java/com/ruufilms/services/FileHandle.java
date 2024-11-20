package com.ruufilms.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandle {

    List<String> filePaths = new ArrayList<>();
    private final String userHome = System.getProperty("user.home");
    private final Path downloadsPath = Paths.get(userHome,"Downloads");
    private final Path filmDownloadFolder = Paths.get(String.valueOf(downloadsPath),"Ruu_Bot","Films");

    private String uploadFilePath;
    private final String [] formats = {
            "MP4", "MKV", "AVI", "MOV", "WMV", "FLV", "WebM", "3GP", "MPEG", "MPEG-2",
            "MPEG-4", "TS", "VOB", "ProRes", "DNxHD", "ASF", "SWF", "OGV", "RM", "RMVB",
            "MXF", "MPG", "DIVX", "XVID", "F4V", "M4V", "QT", "MTS", "M2TS", "DASH", "3G2",
            "MPV", "AMV", "NSV", "MPEG-1", "DV", "BRAW", "H264", "H265", "HEVC", "VP8",
            "VP9", "AV1", "THP", "MPJEG", "IVF", "CAVS", "IV50", "MVP", "FLI", "FLC",
            "ROQ", "SIF", "SKM", "TGV", "TRP", "TY", "VID", "WTV", "XTC", "YUV", "DVR-MS",
            "MT2S", "GXF", "LXF", "PVA", "VC1", "WMV9", "MJPEG", "CINE", "JP2", "Y4M",
            "NUT", "STR", "SVQ1", "SVQ3", "RMVB", "SMI", "DCP", "DASH", "FBR",
            "HDMOV", "AVCHD", "MLV", "EVO", "MXF", "FIC", "PS", "TS", "OGG",
            "VRO", "DVCPRO", "DVCAM","PDF","MP3","EPUB","TXT"
    };
    public FileHandle(){
        File folder = new File(String.valueOf(getFilmDownloadFolder()));
        fileSearch(folder);
    }

    public void CrateDownloadFolders(){
        try{
            if(Files.notExists(filmDownloadFolder)){
                Files.createDirectories(filmDownloadFolder);
            }
        }catch (IOException e){
            System.err.println("Error creating Downloads folder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a folder at the specified path.
     *
     * @param folderPath The path where the folder should be created.
     */

    private void createFolder(String folderPath) {

    }

    /**
     * Searches for a file by its name.
     *
     */

    private List<String> fileSearch(File folder) {
        List<String> filePaths = new ArrayList<>();

        if (folder == null || !folder.isDirectory()) {
            return filePaths;
        }

        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println(file);
                    filePaths.addAll(fileSearch(file));
                } else if (matchesFormat(file.getName())) {
                    filePaths.add(file.getAbsolutePath());
                    System.out.println("File found: " + file.getAbsolutePath());
                }
            }
        }

        return filePaths;
    }

    public List<String> getUploadFilePath(){
        return fileSearch(new File(filmDownloadFolder.toString()));
    }

    private boolean matchesFormat(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return false;
        }

        String extension = fileName.substring(dotIndex + 1).toUpperCase();
        System.out.println("Checking file extension: " + extension); // Log extension being checked
        return Arrays.asList(formats).contains(extension);
    }


    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public File getFilmDownloadFolder() {
        return new File(String.valueOf(filmDownloadFolder));
    }

    public void deleteFolder() {
        File downloadFolder = new File(String.valueOf(getFilmDownloadFolder()));
        File[] files = downloadFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        System.out.println("Failed to delete file: " + file.getName());
                    }
                } else if (file.isDirectory()) {
                    deleteFolderRecursive(file);
                }
            }
        }
    }

    private void deleteFolderRecursive(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        System.out.println("Failed to delete file: " + file.getName());
                    }
                } else {
                    deleteFolderRecursive(file);
                }
            }
        }
        if (!folder.delete()) {
            System.out.println("Failed to delete folder: " + folder.getName());
        }
    }

    public void DeleteFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    DeleteFiles(f);
                }
            }
        }
        if (file.delete()) {
            System.out.println("Deleted: " + file.getAbsolutePath());
        } else {
            System.out.println("Failed to delete: " + file.getAbsolutePath());
        }
    }

}
