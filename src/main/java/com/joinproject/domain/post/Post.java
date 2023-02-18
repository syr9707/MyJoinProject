package com.joinproject.domain.post;

import com.joinproject.domain.BaseTimeEntity;
import com.joinproject.domain.comment.Comment;
import com.joinproject.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 40, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String filePath;

    // 회원 : 게시물 = 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    // 게시글을 삭제하면 댓글도 모두 삭제된다.
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }


    /**
     * 연관관계 편의 메서드
     * */
    public void confirmWriter(Member writer) {
        // writer는 변경이 불가하다.
        this.writer = writer;
        writer.addPost(this);
    }

    public void addComment(Comment comment) {
        // comment의 Post 설정은 comment에서 한다.
        commentList.add(comment);
    }


    // Post 내용 수정
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateFilePath(String filePath) {
        this.filePath = filePath;
    }

}
