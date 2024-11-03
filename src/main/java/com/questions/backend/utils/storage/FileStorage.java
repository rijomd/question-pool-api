package com.questions.backend.utils.storage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.util.stream.Stream;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.questions.backend.exceptions.StorageException;

@Service
public class FileStorage implements StorageService {

    private Path rootLocation;

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("No file found");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new StorageException(e.getMessage());
        }
    }

    @Override
    public void init() {
        try {
            if (rootLocation != null) {
                Files.createDirectories(rootLocation);
            }
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage");
        }
    }

    @Override
    public Path load(String fileName) {
        return rootLocation.resolve(fileName);
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file :" + fileName);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (Exception e) {
            throw new StorageException("Failed to read stored files");
        }
    }

    @Override
    public void deleteAll() {
        if (rootLocation != null) {
            FileSystemUtils.deleteRecursively(rootLocation.toFile());
        }
    }

}
