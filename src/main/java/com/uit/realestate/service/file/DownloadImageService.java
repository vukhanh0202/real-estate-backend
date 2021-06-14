package com.uit.realestate.service.file;

import javax.servlet.http.HttpServletResponse;

public interface DownloadImageService {
    void downloadImage(HttpServletResponse response, String prefix, String fileName);
}
