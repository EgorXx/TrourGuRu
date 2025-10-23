package ru.kpfu.itis.sorokin.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import ru.kpfu.itis.sorokin.dto.ImageUploadResult;
import ru.kpfu.itis.sorokin.service.ImageUploadService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CloudinaryImageUploadService implements ImageUploadService {
    private Cloudinary cloudinary;

    public CloudinaryImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public ImageUploadResult upload(InputStream inputStream, String originalFileName) throws IOException {
        byte[] bytes = inputStream.readAllBytes();

        Map uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());

        String securUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        return new ImageUploadResult(publicId, securUrl);
    }

    @Override
    public void delete(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            return;
        }

        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    @Override
    public void delete(List<String> publicIds) throws Exception {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }

        cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
    }
}
