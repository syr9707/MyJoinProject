package com.joinproject.domain.comment.dto;

import com.joinproject.domain.comment.Comment;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentSaveDto {

    private String content;

    @Builder
    public CommentSaveDto(String content) {
        this.content = content;
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }

}
