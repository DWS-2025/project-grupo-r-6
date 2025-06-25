package com.example.dws.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx", "txt");

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) throws Exception {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public String storeFile(MultipartFile file, Long productId) throws IOException {
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF, DOC, DOCX o TXT");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = productId + "_" + originalFilename;

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public Resource loadFileAsResource(Long productId) throws IOException {
        try (Stream<Path> paths = Files.walk(this.fileStorageLocation)) {
            Optional<Path> filePath = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(productId + "_"))
                    .findFirst();

            if (filePath.isPresent()) {
                Resource resource = new UrlResource(filePath.get().toUri());
                if (resource.exists()) {
                    return resource;
                }
            }
        }

        throw new FileNotFoundException("Archivo no encontrado para el producto: " + productId);
    }

    public String getOriginalFilename(Long productId) throws IOException {
        try (Stream<Path> paths = Files.walk(this.fileStorageLocation)) {
            Optional<Path> filePath = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(productId + "_"))
                    .findFirst();

            if (filePath.isPresent()) {
                String filename = filePath.get().getFileName().toString();
                return filename.substring(filename.indexOf('_') + 1);
            }
        }

        throw new FileNotFoundException("Archivo no encontrado para el producto: " + productId);
    }

    public Path getFileStorageLocation() {
        return this.fileStorageLocation;
    }

    public void deleteFile(Long productId) throws IOException {
        try (Stream<Path> paths = Files.walk(this.fileStorageLocation)) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(productId + "_"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}