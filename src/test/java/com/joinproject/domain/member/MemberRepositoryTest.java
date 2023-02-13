package com.joinproject.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    private void clear(){
        em.flush();
        em.clear();
    }

    @AfterEach
    public void cleanUp() {
        em.clear();
    }

    // 회원저장 성공
    @Test
    public void 회원저장_성공() throws Exception {
        // given
        Member member = Member.builder()
                .username("username")
                .password("1234567890")
                .name("MyName")
                .nickname("MyNickname")
                .role(Role.USER)
                .age(22)
                .build();

        // when
        Member saveMember = memberRepository.save(member);

        // then
        Member findMember = memberRepository.findById(saveMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원 없습니다. userId = " + saveMember.getId()));

        assertThat(findMember).isSameAs(saveMember);
        assertThat(findMember).isSameAs(member);
    }

    // 아이디 없이 회원가입시 오류
    @Test
    public void 오류_회원가입시_아이디가_없음() throws Exception {
        // given
        Member member = Member.builder()
                .password("1234567890")
                .name("MyName")
                .nickname("MyNickname")
                .role(Role.USER)
                .age(22)
                .build();

        // when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    // 회원가입시 중복된 아이디가 있으면 오류
    /**
     * username가 unique로 설정되었기 때문에, username에는 인덱스가 형성되고, 중복을 허용하지 않는 제약조건이 추가됨.
     * */
    @Test
    public void 오류_회원가입시_중복된_아이디가_있음() throws Exception {
        // given
        Member member1 = Member.builder()
                .username("username")
                .password("1234567890")
                .name("MyName1")
                .nickname("MyNickname")
                .role(Role.USER)
                .age(22)
                .build();

        Member member2 = Member.builder()
                .username("username")
                .password("987654321")
                .name("MyName2")
                .nickname("MyNickname")
                .role(Role.USER)
                .age(22)
                .build();

        memberRepository.save(member1);
        clear();

        // when, then
        assertThrows(Exception.class, () -> memberRepository.save(member2));
    }
}