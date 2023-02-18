package com.joinproject.domain.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@NoArgsConstructor
public class PostUpdateDto {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<MultipartFile> uploadFile;

    @Builder
    public PostUpdateDto(Optional<String> title, Optional<String> content, Optional<MultipartFile> uploadFile) {
        this.title = title;
        this.content = content;
        this.uploadFile = uploadFile;
    }

}
