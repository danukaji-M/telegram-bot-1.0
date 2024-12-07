package com.ruufilms.services;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataExtraction {
    private String filePath;
    private final String [] RELEASE_TYPES ={
            "CAM", "TS", "TC", "WEBRip", "WEB-DL", "DVDScr", "DVDRip",
            "BDRip", "BRRip", "HDRip", "HDCAM", "HDTVRip", "PreDVDRip",
            "R5", "Workprint"
    };

    public DataExtraction(String filePath){
        this.filePath = filePath;
    }

    public String getFileName(){
        File file = new File(this.filePath);
        return file.getName();
    }

    public String extractReleaseType(){
        for(String type: RELEASE_TYPES){
            if(getFileName().toUpperCase().contains(type)){
                return type;
            }
        }
        return "Uncknoun";
    }

    public String extractMovieName(){
        String pattern = "(\\d{3,4}p|CAM|TS|TC|WEBRip|WEB-DL|DVDScr|DVDRip|BDRip|BRRip|HDRip|HDCAM|HDTVRip|PreDVDRip|R5|Workprint|BluRay|AMZN|HEVC|x265|\\d{1,4}MB|10bit|6CH|DDP5.1|AAC|ESub)";
        return getFileName().replaceAll(pattern, "").replaceAll("[._]"," ").trim();
    }

    public String extractFileExtension(){
        String fileName = getFileName();
        int lastIndex = fileName.lastIndexOf(".");
        if(lastIndex != -1 && lastIndex != fileName.length() -1){
            return fileName.substring(lastIndex +1).toLowerCase();
        }
        return "Unknown";
    }

    public String extractSize(){
        String pattern = "(\\d{3,4}p)";
        Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(getFileName());
        if(matcher.find()){
            return matcher.group();
        }
        return "Unknown";
    }

    public String fileSizeExtract(){
        File file = new File(this.filePath);
        if(file.exists() && file.isFile()){
            long fileSizeInBytes = file.length();

            double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);
            double fileSizeInGB = fileSizeInMB / 1024.0;

            // Return size in MB if under 1 GB, otherwise in GB
            if (fileSizeInGB >= 1) {
                return String.format("%.2f GB", fileSizeInGB);
            } else {
                return String.format("%.2f MB", fileSizeInMB);
            }
        }
        return "Unknown";
    }
}
