package com.libcommons.file;

import com.libcommons.classes.ServiceException;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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

    public Path saveResource(Resource resource, String fileNamePrefix) throws IOException {
        return saveInputStream(resource.getInputStream(), resource.getFilename(), fileNamePrefix);
    }

    private Path saveInputStream(InputStream inputStream, String originalName, String fileNamePrefix) throws IOException {
        Path baseDir = getOrCreateBaseDirectory();
        String extension = getFileExtension(originalName);
        String safeName = sanitizeFileName(fileNamePrefix) + (extension.isEmpty() ? "" : "." + extension);
        Path targetFile = baseDir.resolve(safeName);
        Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

    public Path getFilePath(String fileName) {
        Path baseDir = getOrCreateBaseDirectory();
        Path filePath = baseDir.resolve(sanitizeFileName(fileName));
        if (!Files.exists(filePath)) {
            return null;
        }
        return filePath;
    }

    public void overwriteFile(Resource resource, String fullFileName) throws IOException {
        overwriteInputStream(resource.getInputStream(), fullFileName);
    }

    private void overwriteInputStream(InputStream inputStream, String fullFileName) throws IOException {
        Path filePath = getFilePath(fullFileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
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
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    public Path saveBufferedImage(BufferedImage image, String fileNamePrefix, String format) throws IOException {
        Path baseDir = getOrCreateBaseDirectory();
        String safeName = sanitizeFileName(fileNamePrefix) + "." + format;
        Path targetFile = baseDir.resolve(safeName);
        ImageIO.write(image, format, targetFile.toFile());
        return targetFile;
    }
}
