package com.jkm.jimkanman.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {
    String saveFile(MultipartFile file);
    void deleteFile(String filename);
    File getFile(String filename);
}
