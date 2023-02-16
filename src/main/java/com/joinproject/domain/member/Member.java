package com.joinproject.domain.member;

import com.joinproject.domain.BaseTimeEntity;
import com.joinproject.domain.comment.Comment;
import com.joinproject.domain.post.Post;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 아이디

    private String password;

    @Column(nullable = false, length = 30)
    private String name; // 실명

    @Column(nullable = false, length = 30)
    private String nickname; // 별명

    @Column(nullable = false, length = 30)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role; // 권한 : USER, ADMIN

    @Column(length = 1000)
    private String refreshToken; // RefreshToken


    // 회원 : 게시글 = 1 : N
    // 회원탈퇴 -> 작성한 게시물, 댓글 모두 삭제한다.
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    // 회원 : 댓글 = 1 : N
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();


    /**
     * 연관관계 편의 메서드
     * */
    public void addPost(Post post) {
        // post의 writer 설정은 post에서 한다.
        postList.add(post);
    }

    public void addComment(Comment comment) {
        // comment의 writer 설정은 post에서 한다.
        commentList.add(comment);
    }



    // 정보 수정
    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickName(String nickname){
        this.nickname = nickname;
    }

    public void updateAge(int age){
        this.age = age;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    // 패스워드 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

}
