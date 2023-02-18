package com.joinproject.domain.post.dto;

import com.joinproject.domain.post.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Data
@NoArgsConstructor
public class PostSaveDto {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private Optional<MultipartFile> uploadFile;

    @Builder
    public PostSaveDto(String title, String content, Optional<MultipartFile> uploadFile) {
        this.title = title;
        this.content = content;
        this.uploadFile = uploadFile;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

}
