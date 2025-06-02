package com.rentals.rentalapp.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        // Define the path to the upload directory within static resources
        // Files stored here will be accessible via /uploads/filename.ext
        String uploadDir = "src/main/resources/static/uploads";
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
        } catch (Exception e) {
            // Log error or handle, for now, ignore if no extension
        }

        // Generate a unique file name to prevent conflicts
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Check if the file's name contains invalid characters
        if (uniqueFileName.contains("..")) {
            throw new IOException("Sorry! Filename contains invalid path sequence " + originalFileName);
        }

        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative URL path to access the file
        return "/uploads/" + uniqueFileName;
    }
}
