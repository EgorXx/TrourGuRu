package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.ImageUploadResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ImageUploadService {
    ImageUploadResult upload(InputStream inputStream, String originalFileName) throws IOException;

    void delete(String publicId)  throws IOException;

    void delete(List<String> publicIds) throws Exception;
}
