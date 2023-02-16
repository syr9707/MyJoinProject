package com.joinproject.domain.comment.service;

import com.joinproject.domain.comment.Comment;
import com.joinproject.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment findById(Long id) throws Exception {
        return commentRepository.findById(id).orElseThrow(
                () -> new Exception("댓글이 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public void remove(Long id) throws Exception {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new Exception("댓글이 없습니다.")
        );
        comment.remove();
        List<Comment> removableCommentList = comment.findRemovableList();
        removableCommentList.forEach(removableComment -> commentRepository.delete(removableComment));
    }
}
