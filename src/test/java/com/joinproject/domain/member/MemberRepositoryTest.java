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
}