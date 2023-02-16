package com.joinproject.domain.comment;

import com.joinproject.domain.BaseTimeEntity;
import com.joinproject.domain.member.Member;
import com.joinproject.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean isRemoved = false;


    // 회원 : 댓글 = 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    // 게시글 : 댓글 = 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 댓글 : 대댓글 = 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;


    // 부모 댓글을 삭제해도 자식 댓글은 남아있다.
    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();


    /**
     * 연관관계 편의 메서드
     * */
    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addComment(this);
    }

    public void confirmPost(Post post) {
        this.post = post;
        post.addComment(this);
    }

    public void addChild(Comment child) {
        childList.add(child);
    }

    public void confirmParent(Comment parent) {
        this.parent = parent;
        parent.addChild(this);
    }


    // Comment 수정
    public void updateContent(String content) {
        this.content = content;
    }

    // Comment 삭제
    public void remove() {
        this.isRemoved = true;
    }

    @Builder
    public Comment(Member writer, Post post, Comment parent, String content) {
        this.writer = writer;
        this.post = post;
        this.parent = parent;
        this.content = content;
        this.isRemoved = false;
    }

}
