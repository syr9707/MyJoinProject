package com.joinproject.domain.member;

import com.joinproject.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

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
