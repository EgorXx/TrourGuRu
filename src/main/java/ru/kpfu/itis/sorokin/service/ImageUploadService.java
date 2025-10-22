package ru.kpfu.itis.sorokin.service;

import java.io.IOException;
import java.io.InputStream;

public interface ImageUploadService {
    String upload(InputStream inputStream, String originalFileName) throws IOException;
}
