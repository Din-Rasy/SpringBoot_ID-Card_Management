package net.orderzone.idcard.service;

import net.orderzone.idcard.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PhotoStorageService {

    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg"))) {
            throw new StorageException("Only JPEG and PNG images are allowed.");
        }

        // Validate size: 2MB limit (2 * 1024 * 1024 bytes)
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new StorageException("File size exceeds the maximum limit of 2MB.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = contentType.equals("image/png") ? ".png" : ".jpg";
            }

            String uniqueName = UUID.randomUUID().toString() + extension;
            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueName))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return uniqueName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public void delete(String filename) {
        if (filename == null || filename.isBlank()) {
            return;
        }
        try {
            Path file = load(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            // Ignored, safe fallback
        }
    }
}
