package com.joinproject.domain.comment.service;

import com.joinproject.domain.comment.Comment;
import com.joinproject.domain.comment.dto.CommentSaveDto;
import com.joinproject.domain.comment.dto.CommentUpdateDto;
import com.joinproject.domain.comment.exception.CommentException;
import com.joinproject.domain.comment.exception.CommentExceptionType;
import com.joinproject.domain.comment.repository.CommentRepository;
import com.joinproject.domain.member.exception.MemberException;
import com.joinproject.domain.member.exception.MemberExceptionType;
import com.joinproject.domain.member.repository.MemberRepository;
import com.joinproject.domain.post.exception.PostException;
import com.joinproject.domain.post.exception.PostExceptionType;
import com.joinproject.domain.post.repository.PostRepository;
import com.joinproject.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    @Override
    public void save(Long postId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUserName()).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER))
        );

        comment.confirmPost(postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostExceptionType.POST_NOT_POUND))
        );

        commentRepository.save(comment);
    }

    @Override
    public void saveReComment(Long postId, Long parentId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUserName()).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER))
        );

        comment.confirmPost(postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostExceptionType.POST_NOT_POUND))
        );

        comment.confirmParent(commentRepository.findById(parentId).orElseThrow(
                () -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT))
        );

        commentRepository.save(comment);
    }

    @Override
    public void update(Long id, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT)
        );

        if(!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUserName())){
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_UPDATE_COMMENT);
        }

        commentUpdateDto.getContent().ifPresent(comment::updateContent);
    }

    @Override
    public void remove(Long id) throws CommentException {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT)
        );

        if(!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUserName())){
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }

        comment.remove();
        List<Comment> removableCommentList = comment.findRemovableList();
        commentRepository.deleteAll(removableCommentList);
    }
}
