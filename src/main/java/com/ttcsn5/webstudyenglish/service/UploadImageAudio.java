package com.ttcsn5.webstudyenglish.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImageAudio {
    public String upload(MultipartFile image, String type) throws IOException {
        String file = null;
        String filePathStr = null;
        if (image != null && !image.isEmpty()) {
            String uniqueId = UUID.randomUUID().toString();
            file = image.getOriginalFilename();
            Path imgDir = Paths.get("uploads/" + type);
            if (!Files.exists(imgDir)) {
                Files.createDirectories(imgDir);
            }
            if (file != null) {
                String imgFileName = uniqueId + "__" + file;
                Path imgPath = imgDir.resolve(imgFileName);
                Files.write(imgPath, image.getBytes());
                // Lưu đường dẫn public (bằng /uploads/...) để frontend có thể truy cập được
                filePathStr = "/uploads/" + type + "/" + imgFileName;
            }
        }

        return filePathStr;
    }

    // nen tnag

    // String aud = null;
    // String audPathStr = null;
    // if (audio != null && !audio.isEmpty()) {
    // String uniqueId = UUID.randomUUID().toString();
    // aud = audio.getOriginalFilename();
    // Path audDir = Paths.get("uploads/audios");

    // if (!Files.exists(audDir)) {
    // Files.createDirectories(audDir);
    // }

    // if (aud != null) {
    // String audFileName = uniqueId + "__" + aud;
    // Path audPath = audDir.resolve(audFileName);
    // Files.write(audPath, audio.getBytes());
    // audPathStr = "/uploads/audios/" + audFileName;
    // }
    // }
}
