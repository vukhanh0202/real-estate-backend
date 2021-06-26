package com.uit.realestate.service.file.impl;

import com.uit.realestate.configuration.property.ApplicationProperties;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.service.file.UploadService;
import com.uit.realestate.utils.FileHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ApplicationProperties app;

    @Override
    public Set<FileCaption> uploadPhoto(MultipartFile[] files, String prefix, Long id) {
        try {
            Set<FileCaption> fileNames = new HashSet<>();
            if (files != null && files.length > 0) {
                Arrays.asList(files).forEach(file -> {
                    File fileClone = new File(app.getSource().getDirectory() + prefix + FileHandler.generateFileName(id, file.getOriginalFilename()));
                    FileCaption photoCaption = FileHandler.formatImage(file.getOriginalFilename(), fileClone);
                    try {
                        File fileDirectory = new File(app.getSource().getDirectory() + prefix);
                        if (!fileDirectory.exists()) {
                            if (fileDirectory.mkdirs()) {
//                                fileClone.createNewFile();
                                file.transferTo(fileClone.toPath());
                            } else {
                                throw new InvalidException("Can not create folder");
                            }
                        } else {
//                            fileClone.createNewFile();
                            file.transferTo(fileClone.toPath());
                        }

//                        Files.copy(file.getInputStream(), fileClone.toPath());
                        fileNames.add(photoCaption);
                    } catch (Exception e) {
                        throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
                    }

                });
            }
            fileNames.forEach(fileCaption -> {
                log.info("Upload Success:" + fileCaption.getName());
            });
            return new HashSet<>(fileNames);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Set<FileCaption> uploadPhoto(MultipartFile file, String prefix, Long id) {
        try {
            Set<FileCaption> fileNames = new HashSet<>();
            if (file != null) {
                File fileClone = new File(app.getSource().getDirectory() + prefix + FileHandler.generateFileName(id, file.getOriginalFilename()));
                FileCaption photoCaption = FileHandler.formatImage(file.getOriginalFilename(), fileClone);
                try {
                    File fileDirectory = new File(app.getSource().getDirectory() + prefix);
                    if (!fileDirectory.exists()) {
                        if (fileDirectory.mkdirs()) {
                            file.transferTo(fileClone.toPath());
                        } else {
                            throw new InvalidException("Can not create folder");
                        }
                    } else {
                        file.transferTo(fileClone.toPath());
                    }
                    fileNames.add(photoCaption);
                } catch (Exception e) {
                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
                }
            }
            fileNames.forEach(fileCaption -> {
                log.info("Upload Success:" + fileCaption.getName());
            });
            return new HashSet<>(fileNames);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
