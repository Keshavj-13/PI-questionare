package com.pi.qna.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Component
public class LocalFileStorageClient implements FileStorageClient {

    private final String uploadDir = "uploads"; // local folder

    @Override
    public String upload(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), path);
            return "/files/" + filename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String url) {
        String filename = Paths.get(url).getFileName().toString();
        try { Files.deleteIfExists(Paths.get(uploadDir, filename)); } catch (IOException ignored) {}
    }
}
