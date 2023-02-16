package com.joinproject.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joinproject.domain.member.Member;
import com.joinproject.domain.member.Role;
import com.joinproject.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JwtServiceTest {

    @Autowired
    JwtService jwtService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";

    private String username = "username";

    private void clear(){
        em.flush();
        em.clear();
    }

    @BeforeEach
    public void init(){
        Member member = Member.builder()
                .username(username)
                .password("1234567890")
                .name("Member1")
                .nickname("NickName1")
                .role(Role.USER)
                .age(22)
                .build();

        memberRepository.save(member);
        clear();
    }

    private DecodedJWT getVerify(String token) {
        return JWT.require(HMAC512(secret)).build().verify(token);
    }

    // AccessToken 발급 테스트
    @Test
    void createAccessToken_AccessToken_발급() throws Exception {
        //given, when
        String accessToken = jwtService.createAccessToken(username);

        DecodedJWT verify = getVerify(accessToken);

        String subject = verify.getSubject();
        String findUsername = verify.getClaim(USERNAME_CLAIM).asString();

        //then
        assertThat(findUsername).isEqualTo(username);
        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
    }

    // RefreshToken 발급 테스트
    // refreshToken은 username이 없어야 한다.
    @Test
    public void createRefreshToken_RefreshToken_발급() throws Exception {
        //given, when
        String refreshToken = jwtService.createRefreshToken();
        DecodedJWT verify = getVerify(refreshToken);
        String subject = verify.getSubject();
        String username = verify.getClaim(USERNAME_CLAIM).asString();

        //then
        assertThat(subject).isEqualTo(REFRESH_TOKEN_SUBJECT);
        assertThat(username).isNull();
    }

    // RefreshToken 업데이트
    @Test
    public void updateRefreshToken_refreshToken_업데이트() throws Exception {
        //given
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(username, refreshToken);
        clear();
        Thread.sleep(3000); // 3초 sleep 하지 않으면, refreshToken이 똑같이 발급될 수 있음

        //when
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(username, reIssuedRefreshToken);
        clear();

        //then
        assertThrows(Exception.class, () -> memberRepository.findByRefreshToken(refreshToken).get());//
        assertThat(memberRepository.findByRefreshToken(reIssuedRefreshToken).get().getUsername()).isEqualTo(username);
    }

    // RefreshToken 제거
    @Test
    public void destroyRefreshToken_refreshToken_제거() throws Exception {
        //given
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(username, refreshToken);
        clear();

        //when
        jwtService.destroyRefreshToken(username);
        clear();

        //then
        assertThrows(Exception.class, () -> memberRepository.findByRefreshToken(refreshToken).get());

        Member member = memberRepository.findByUsername(username).get();
        assertThat(member.getRefreshToken()).isNull();
    }

}