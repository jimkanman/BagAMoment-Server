package com.jkm.jimkanman.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DiskFileService implements FileService {
    // TODO
    @Override
    public String saveFile(MultipartFile file) {
        // store용 filename 생성하여 return
        return null;
    }

    @Override
    public void deleteFile(String filename) {

    }

    @Override
    public MultipartFile getFile(String filename) {
        return null;
    }
}
