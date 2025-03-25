package com.example.orderproduct.utils;

import org.springframework.web.multipart.MultipartFile;

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
}
