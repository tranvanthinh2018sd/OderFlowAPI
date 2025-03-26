package com.example.orderproduct.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class CommonUtils {

    public static String saveImage(MultipartFile file) {
        String imageUrl = null;
        String uploadDir = "../image";

        if(file != null && !file.isEmpty()) {
            try{
                Path uploadPath = Paths.get(uploadDir);

                // nếu thư mục chưa tồn tại
                if(!Files.exists(uploadPath)) {
                    Files.createDirectory(uploadPath);
                }
                String fileName = UUID.randomUUID() +"_"+file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);
                Files.copy(Paths.get(file.getOriginalFilename()), filePath);
                return fileName;
            }
            catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
    return imageUrl;
    }

    public static boolean isNullOrEmpty(MultipartFile fileName) {
        if(fileName == null && fileName.isEmpty()) {
            return true;
        }
        else{
            return false;
        }
    }

    // Hàm chuyển MultipartFile -> Base64
    public static String convertToBase64(MultipartFile file) throws IOException {
        byte[] fileBytes;
        try (InputStream inputStream = file.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        }
        return Base64.encodeBase64String(fileBytes);
    }
}
