package com.uit.realestate.utils;

import com.google.common.io.Files;
import com.uit.realestate.dto.response.FileCaption;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileHandler {
    private static String MAIN_DIRECTORY;
    private static final Integer MAX_RANDOM=100000000;

    public static FileCaption formatImage(String originalName, File file) {
        FileCaption photoCaption = new FileCaption();
        photoCaption.setOriginalName(originalName);
        photoCaption.setName(file.getName());
        photoCaption.setExtension(Files.getFileExtension(originalName));
        return  photoCaption;
    }

    public static String generateFileName(Long userId, String originalName) {
        Date myDate = new Date();
        return userId + "_" + new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss").format(myDate) + "-"+(int)Math.ceil(Math.random()*MAX_RANDOM) +"." + Files.getFileExtension(originalName);
    }

    public static String generateFileName(String originalName) {
        Date myDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss").format(myDate) + "-"+(int)Math.ceil(Math.random()*MAX_RANDOM) +"." + Files.getFileExtension(originalName);
    }

    public static MultipartFile[] downloadFileFromUrls(List<String> urls){
        if (Objects.isNull(urls)){
            return new MultipartFile[0];
        }
        List<MultipartFile> result = new ArrayList<>();
        for (String url: urls) {
            File file = new File(UUID.randomUUID() + "." + Files.getFileExtension(url));
            try {
                FileUtils.copyURLToFile(new URL(url), file);
                FileInputStream input = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile("file",
                        file.getName(), "image/jpeg", IOUtils.toByteArray(input));
                result.add(multipartFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toArray(new MultipartFile[0]);
    }
}
