package com.joinproject.domain.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class CommentUpdateDto {

    private Optional<String> content;

    @Builder
    public CommentUpdateDto(Optional<String> content) {
        this.content = content;
    }

}
