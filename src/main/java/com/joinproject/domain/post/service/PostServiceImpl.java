package com.joinproject.domain.post.service;

import com.joinproject.domain.member.exception.MemberException;
import com.joinproject.domain.member.exception.MemberExceptionType;
import com.joinproject.domain.member.repository.MemberRepository;
import com.joinproject.domain.post.Post;
import com.joinproject.domain.post.cond.PostSearchCondition;
import com.joinproject.domain.post.dto.PostInfoDto;
import com.joinproject.domain.post.dto.PostPagingDto;
import com.joinproject.domain.post.dto.PostSaveDto;
import com.joinproject.domain.post.dto.PostUpdateDto;
import com.joinproject.domain.post.exception.PostException;
import com.joinproject.domain.post.exception.PostExceptionType;
import com.joinproject.domain.post.repository.PostRepository;
import com.joinproject.global.file.exception.FileException;
import com.joinproject.global.file.service.FileService;
import com.joinproject.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Override
    public void save(PostSaveDto postSaveDto) throws FileException {
        Post post = postSaveDto.toEntity();

        post.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUserName())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        postSaveDto.getUploadFile().ifPresent(
                file ->  post.updateFilePath(fileService.save(file))
        );

        postRepository.save(post);
    }

    @Override
    public void update(Long id, PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostException(PostExceptionType.POST_NOT_POUND));

        checkAuthority(post,PostExceptionType.NOT_AUTHORITY_UPDATE_POST );

        postUpdateDto.getTitle().ifPresent(post::updateTitle);
        postUpdateDto.getContent().ifPresent(post::updateContent);


        if(post.getFilePath() !=null){
            fileService.delete(post.getFilePath());//기존에 올린 파일 지우기
        }

        postUpdateDto.getUploadFile().ifPresentOrElse(
                multipartFile ->  post.updateFilePath(fileService.save(multipartFile)),
                () ->  post.updateFilePath(null)
        );
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostException(PostExceptionType.POST_NOT_POUND));

        checkAuthority(post,PostExceptionType.NOT_AUTHORITY_DELETE_POST);


        if(post.getFilePath() !=null){
            fileService.delete(post.getFilePath());//기존에 올린 파일 지우기
        }

        postRepository.delete(post);
    }

    private void checkAuthority(Post post, PostExceptionType postExceptionType) {
        if(!post.getWriter().getUsername().equals(SecurityUtil.getLoginUserName()))
            throw new PostException(postExceptionType);
    }

    @Override
    public PostInfoDto getPostInfo(Long id) {
        return null;
    }

    @Override
    public PostPagingDto getPostList(Pageable pageable, PostSearchCondition postSearchCondition) {
        return null;
    }
}
