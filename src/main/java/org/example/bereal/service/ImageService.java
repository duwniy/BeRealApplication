package org.example.bereal.service;

import org.example.bereal.config.FileStorageConfig;
import org.example.bereal.exception.ImageUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final Path fileStorageLocation;

    @Value("${server.port:8080}")
    private String serverPort;

    public ImageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath()
                .normalize();
    }

    /**
     * Загружает изображение на сервер
     */
    public String uploadImage(MultipartFile file, Long userId, String type) {
        log.info("Uploading {} image for user {}", type, userId);

        // Валидация
        validateImage(file);

        try {
            // Создаём уникальное имя файла
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String newFileName = String.format("%d_%s_%s%s",
                    userId,
                    type,
                    UUID.randomUUID().toString(),
                    fileExtension
            );

            // Путь для сохранения
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);

            // Копируем файл
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Генерируем URL для доступа к файлу
            String fileUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/images/")
                    .path(newFileName)
                    .toUriString();

            log.info("Image uploaded successfully: {}", fileUrl);
            return fileUrl;

        } catch (IOException e) {
            log.error("Failed to upload image", e);
            throw new ImageUploadException("Failed to upload image: " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет изображение с сервера
     */
    public void deleteImage(String imageUrl) {
        try {
            // Извлекаем имя файла из URL
            String fileName = extractFileNameFromUrl(imageUrl);

            if (fileName == null) {
                log.warn("Could not extract filename from URL: {}", imageUrl);
                return;
            }

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            // Проверяем, что файл находится в разрешённой директории
            if (!filePath.startsWith(this.fileStorageLocation)) {
                log.error("Attempted to delete file outside storage directory: {}", filePath);
                return;
            }

            Files.deleteIfExists(filePath);
            log.info("Image deleted: {}", fileName);

        } catch (IOException e) {
            log.error("Failed to delete image: {}", imageUrl, e);
        }
    }

    /**
     * Валидация изображения
     */
    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Максимальный размер 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        // Проверяем расширение
        String filename = file.getOriginalFilename();
        if (filename == null || !isValidImageExtension(filename)) {
            throw new IllegalArgumentException("Invalid image file extension");
        }
    }

    /**
     * Проверяет допустимое расширение файла
     */
    private boolean isValidImageExtension(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        return extension.equals(".jpg") ||
                extension.equals(".jpeg") ||
                extension.equals(".png") ||
                extension.equals(".gif") ||
                extension.equals(".webp");
    }

    /**
     * Извлекает расширение файла
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : filename.substring(lastDotIndex);
    }

    /**
     * Извлекает имя файла из URL
     */
    private String extractFileNameFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        // Пример URL: http://localhost:8080/images/123_primary_uuid.jpg
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastSlashIndex + 1);
        }

        return null;
    }
}