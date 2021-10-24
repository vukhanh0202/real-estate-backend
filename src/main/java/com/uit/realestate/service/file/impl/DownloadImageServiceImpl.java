package com.uit.realestate.service.file.impl;

import com.uit.realestate.configuration.property.ApplicationProperties;
import com.uit.realestate.service.file.DownloadImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownloadImageServiceImpl implements DownloadImageService {

    private final ApplicationProperties app;

    @Override
    public void downloadImage(HttpServletResponse response, String prefix, String fileName) {
        Path file = Paths.get(app.getSource().getDirectory() + prefix, fileName);

        if (Files.exists(file)) {
            response.setContentType("image/png");
            response.addHeader("Content-Type", "image/jpeg");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}