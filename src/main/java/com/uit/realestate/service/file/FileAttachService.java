package com.uit.realestate.service.file;

import javax.servlet.http.HttpServletResponse;

public interface FileAttachService {
    void downloadFile(HttpServletResponse response, String prefix, String filename, String extension);

}
