package com.libcommons.file;

import com.libcommons.classes.ServiceException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Component
@NoArgsConstructor
public class FileHelper {

    private String baseDirName = "files";

    public FileHelper(String baseDirName) {
        this.baseDirName = baseDirName;
    }

    public Path getOrCreateBaseDirectory() {
        Path baseDir = Paths.get(baseDirName);
        if (Files.notExists(baseDir)) {
            try {
                Files.createDirectories(baseDir);
            } catch (IOException e) {
                throw new ServiceException("Erro ao criar diretório: " + baseDir, "FileHelper", "getOrCreateBaseDirectory", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return baseDir;
    }

    public Path saveStringToFile(String fileName, String extension, String content) throws IOException {
        Path baseDir = getOrCreateBaseDirectory();
        String safeName = sanitizeFileName(fileName) + "." + extension;
        Path targetFile = baseDir.resolve(safeName);
        Files.writeString(targetFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return targetFile;
    }

    public Path saveMultipartFile(MultipartFile file, String fileNamePrefix) throws IOException {
        Path baseDir = getOrCreateBaseDirectory();
        String extension = getFileExtension(file.getOriginalFilename());
        String safeName = sanitizeFileName(fileNamePrefix) + (extension.isEmpty() ? "" : "." + extension);
        Path targetFile = baseDir.resolve(safeName);
        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

    public Path getFilePath(String fileName) {
        Path baseDir = getOrCreateBaseDirectory();
        Path filePath = baseDir.resolve(sanitizeFileName(fileName));
        if (!Files.exists(filePath)) {
            throw new ServiceException("Arquivo não encontrado: " + fileName, "FileHelper", "getFilePath", HttpStatus.NOT_FOUND);
        }
        return filePath;
    }

    public void overwriteFile(MultipartFile file, String fullFileName) throws IOException {
        Path filePath = getFilePath(fullFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean deleteFile(Path filePath) {
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}
