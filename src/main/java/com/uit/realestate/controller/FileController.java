package com.uit.realestate.controller;

import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.service.file.DownloadImageService;
import com.uit.realestate.service.file.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static com.uit.realestate.constant.AppConstant.APARTMENT_FILE;
import static com.uit.realestate.constant.AppConstant.AVATAR_FILE;

@RestController
@Slf4j
@Api(value = "Files APIs")
@RequiredArgsConstructor
public class FileController {

    private final DownloadImageService downloadImageService;

    private final UploadService uploadService;


    /**
     * Download apartment photo
     */
    @ApiOperation(value = "Download apartment photo")
    @GetMapping("/public/image/apartment/{id}_{filename}.{extension}")
    public void downloadApartment(HttpServletResponse response,
                               @PathVariable("id") Long id,
                               @PathVariable("filename") String filename,
                               @PathVariable("extension") String extension) {
        log.info("Download apartment photo");
        String fileName = id + "_" + filename + "." + extension;
        downloadImageService.downloadImage(response, APARTMENT_FILE, fileName);
    }

    /**
     * Download apartment photo
     */
    @ApiOperation(value = "Download avatar photo")
    @GetMapping("/public/image/avatar/{id}_{filename}.{extension}")
    public void downloadAvatar(HttpServletResponse response,
                               @PathVariable("id") Long id,
                               @PathVariable("filename") String filename,
                               @PathVariable("extension") String extension) {
        log.info("Download avatar photo");
        String fileName = id + "_" + filename + "." + extension;
        downloadImageService.downloadImage(response, AVATAR_FILE, fileName);
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
