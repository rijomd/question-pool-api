package com.questions.backend.utils.storage;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void store(MultipartFile file);

    void init();

    Path load(String fileName);

    Resource loadAsResource(String fileName);

    Stream<Path> loadAll();

    void deleteAll();
}
