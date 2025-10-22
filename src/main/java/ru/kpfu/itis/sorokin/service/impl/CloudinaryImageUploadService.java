package ru.kpfu.itis.sorokin.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import ru.kpfu.itis.sorokin.service.ImageUploadService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class CloudinaryImageUploadService implements ImageUploadService {
    private Cloudinary cloudinary;

    public CloudinaryImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(InputStream inputStream, String originalFileName) throws IOException {
        byte[] bytes = inputStream.readAllBytes();

        Map uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());

        return (String) uploadResult.get("secure_url");
    }
}
