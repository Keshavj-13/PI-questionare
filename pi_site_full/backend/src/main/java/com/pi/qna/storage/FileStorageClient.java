package com.pi.qna.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageClient {
    String upload(MultipartFile file);
    void delete(String url);
}
