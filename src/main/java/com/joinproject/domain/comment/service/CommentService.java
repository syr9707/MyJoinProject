package com.joinproject.domain.comment.service;

import com.joinproject.domain.comment.Comment;
import com.joinproject.domain.comment.dto.CommentSaveDto;
import com.joinproject.domain.comment.dto.CommentUpdateDto;
import com.joinproject.domain.comment.exception.CommentException;

import java.util.List;

public interface CommentService {

    void save(Long postId , CommentSaveDto commentSaveDto);
    void saveReComment(Long postId, Long parentId ,CommentSaveDto commentSaveDto);

    void update(Long id, CommentUpdateDto commentUpdateDto);

    void remove(Long id) throws CommentException;

}
