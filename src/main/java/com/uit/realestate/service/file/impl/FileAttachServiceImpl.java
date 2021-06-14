package com.uit.realestate.service.file.impl;

import com.uit.realestate.configuration.property.ApplicationProperties;
import com.uit.realestate.service.file.FileAttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileAttachServiceImpl implements FileAttachService {

    @Autowired
    private ApplicationProperties app;

    @Override
    public void downloadFile(HttpServletResponse response, String prefix, String filename, String extension) {
        Path file = Paths.get(app.getSource().getDirectory() + prefix, filename + "." + extension);
        if (Files.exists(file)) {
            response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "." + extension + "\"");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
