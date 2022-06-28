package com.example.bookshop_app.data;

import com.example.bookshop_app.entity.book.file.BookFile;
import com.example.bookshop_app.repo.BookFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class ResourceStorage {

    private static final Logger logger = LoggerFactory.getLogger(ResourceStorage.class);

    @Value("${upload.path}")
    String uploadPath;
    @Value("${download.path}")
    String downloadPath;

    @Autowired
    private BookFileRepository bookFileRepository;

    public ResourceStorage() {
        super();
    }

    public String saveNewBookImage(MultipartFile file, String slug) throws IOException {
        String resourceURI = null;

        if (!file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));
                logger.info("created image folder in {}", uploadPath);
            }

            String fileName = slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/book_covers/" + fileName;
            file.transferTo(path);
            logger.info("fileName {}", "uploaded successfully");
        }
        return resourceURI;
    }

    public Path getBookFilePath(String hash) {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());
        if (Objects.nonNull(mimeType)) {
            return MediaType.parseMediaType(mimeType);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        return Files.readAllBytes(path);
    }
}