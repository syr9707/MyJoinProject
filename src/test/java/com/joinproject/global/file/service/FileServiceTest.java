package com.joinproject.global.file.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    FileService fileService;

    private MockMultipartFile getMockUploadFile() throws IOException {
        return new MockMultipartFile("a", "a.jpg", "image/jpg", new FileInputStream("C:\\myProject\\testfile\\a.jpg"));
    }

    @Test
    public void 파일저장_성공() throws Exception {
        //given, when
        String filePath = fileService.save(getMockUploadFile());

        //then
        File file = new File(filePath);
        assertThat(file.exists()).isTrue();

        //finally
        file.delete();//파일 삭제

    }

    @Test
    public void 파일삭제_성공() throws Exception {
        //given, when
        String filePath = fileService.save(getMockUploadFile());
        fileService.delete(filePath);

        //then
        File file = new File(filePath);
        assertThat(file.exists()).isFalse();

    }

}