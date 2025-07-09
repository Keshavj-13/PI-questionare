package com.pi.qna.service;

import com.pi.qna.entity.Image;
import com.pi.qna.entity.Question;
import com.pi.qna.repository.ImageRepository;
import com.pi.qna.repository.QuestionRepository;
import com.pi.qna.storage.FileStorageClient;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final QuestionRepository questionRepo;
    private final ImageRepository imageRepo;
    private final FileStorageClient storage; // your own component

    @Transactional
    public Image attachImage(long questionId, MultipartFile file) {
        Question q = questionRepo.getReferenceById(questionId);

        String url = storage.upload(file);  // AWS S3, local FS, etc.

        Image img = new Image();
        img.setUrl(url);
        img.setQuestion(q);
        return imageRepo.save(img);
    }
}
