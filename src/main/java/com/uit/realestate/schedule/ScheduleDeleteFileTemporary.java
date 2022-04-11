package com.uit.realestate.schedule;

import com.uit.realestate.configuration.property.ApplicationProperties;
import com.uit.realestate.constant.AppConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleDeleteFileTemporary {

    private final ApplicationProperties app;

    @Scheduled(initialDelay = 1000, fixedDelay = Long.MAX_VALUE)
    public void deleteFileTemporary() {
        File folderDelete = FileUtils.getFile(app.getSource().getDirectory() + "/deleteFiles/");
        if (folderDelete.exists() && folderDelete.listFiles() != null && folderDelete.listFiles().length > 0) {
            List<File> files = Arrays.asList(Objects.requireNonNull(folderDelete.listFiles()));
            if (files.size() > 500){
                files = files.subList(0, 500);
            }
            log.info("Job delete file temporary start with size: " + files.size());
            files.forEach(fileTemp -> {
                File file1 = FileUtils.getFile(fileTemp.getName());
                boolean action = true;
                if (file1.exists()) {
                    boolean res = file1.delete();
                    if (!res) action = false;
                }
                File file2 = FileUtils.getFile(app.getSource().getDirectory() + AppConstant.APARTMENT_FILE + fileTemp.getName());
                if (file2.exists()) {
                    boolean res = file2.delete();
                    if (!res) action = false;
                }
                if (action) {
                    fileTemp.delete();
                }
            });
            log.info("Finish job delete file temporary");
        }
    }
}
