package com.jkm.jimkanman.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class DiskFileService implements FileService {
    private final String savePath;

    DiskFileService(@Value("${file-save-path}")String savePath){
        this.savePath = savePath;
    }

    // TODO
    @Override
    public String saveFile(MultipartFile file) {
        // store용 filename 생성하여 return
        if(file == null || file.isEmpty()) return null;
        String originalFilename = file.getOriginalFilename();
        String randomFilename = (originalFilename == null ? "file" : originalFilename) + "_" + UUID.randomUUID();
        try {
            file.transferTo(Path.of(savePath, randomFilename));
        } catch (IOException e){
            System.out.println("DiskFileService: IOException while saving " + file.getOriginalFilename());
            e.printStackTrace();
            return null;
        }
        return randomFilename;
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Files.delete(Path.of(savePath, filename));
        }
        catch (NoSuchFileException e) {
            System.out.println("DiskFileService: NoSuchFileException while deleting " + filename);
            System.out.println("DiskFileService: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println("DiskFileService: IOException while saving " + filename);
            e.printStackTrace();
        }
    }

    @Override
    public File getFile(String filename) {
        // 파일 경로 생성 및 파일 객체 반환
        File file = new File(savePath + File.separator + filename);
        if (file.exists() && file.isFile()) {
            return file;
        } else {
            System.out.println("DiskFileService: File not found '" + filename + "'");
            return null;
        }
    }
}
