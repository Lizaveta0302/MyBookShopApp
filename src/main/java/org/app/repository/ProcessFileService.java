package org.app.repository;

import org.apache.log4j.Logger;
import org.app.exception.UploadFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

//FIXME Service в repository?
@Service
public class ProcessFileService {
    private final Logger logger = Logger.getLogger(ProcessFileService.class);

    public void uploadFile(MultipartFile file) throws IOException {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new UploadFileException("File upload error, file is empty");
        }
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        //FIXME двойная вложенность if? Исправить на линейную структуру один if одна обработка
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                logger.info("directory is not created");
                throw new UploadFileException("Directory is not created");
            }
        }
        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        //FIXME исправить через try-with-resources
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();
        logger.info("new file saved at: " + serverFile.getAbsolutePath());
    }
}
