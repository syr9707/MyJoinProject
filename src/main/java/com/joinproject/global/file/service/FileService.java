package com.joinproject.global.file.service;

import com.joinproject.global.file.exception.FileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface FileService {

    //저장된 파일 경로 반환
    String save(MultipartFile multipartFile) throws FileException;

    void delete(String filePath);

}
