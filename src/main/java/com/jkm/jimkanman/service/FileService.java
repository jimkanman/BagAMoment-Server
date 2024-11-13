package com.jkm.jimkanman.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file);
    void deleteFile(String filename);
    MultipartFile getFile(String filename);
}
