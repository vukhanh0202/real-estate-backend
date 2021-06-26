package com.uit.realestate.service.file;

import com.uit.realestate.dto.response.FileCaption;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface UploadService {
    Set<FileCaption> uploadPhoto(MultipartFile[] files, String prefix, Long id);
    Set<FileCaption> uploadPhoto(MultipartFile files, String prefix, Long id);
}
