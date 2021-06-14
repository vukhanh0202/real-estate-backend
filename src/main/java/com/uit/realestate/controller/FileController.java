package com.uit.realestate.controller;

import com.google.common.collect.Lists;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.service.file.DownloadImageService;
import com.uit.realestate.service.file.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Set;

@RestController
@Slf4j
@Api(value = "Files APIs")
public class FileController {

    private static final String APARTMENT_FILE = "/apartment/";

    @Autowired
    private DownloadImageService downloadImageService;

    @Autowired
    private UploadService uploadService;


    /**
     * Download apartment photo
     */
    @ApiOperation(value = "Download apartment photo")
    @GetMapping("/public/image/apartment/{id}_{filename}.{extension}")
    public void downloadAvatar(HttpServletResponse response,
                               @PathVariable("id") Long id,
                               @PathVariable("filename") String filename,
                               @PathVariable("extension") String extension) {
        log.info("Download apartment photo");
        String fileName = id + "_" + filename + "." + extension;
        downloadImageService.downloadImage(response, APARTMENT_FILE, fileName);
    }

    /**
     * Update photo
     */
    @ApiOperation(value = "Update photo", authorizations = {@Authorization(value = "JWT")})
    @PostMapping(value = "/upload/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("files") MultipartFile[] files) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Connect to /upload/photo");
        Set<FileCaption> photos = uploadService.uploadPhoto(files, APARTMENT_FILE, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(photos));

    }
}
