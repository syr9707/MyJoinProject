package com.joinproject.domain.member.service;

import com.joinproject.domain.member.Member;
import com.joinproject.domain.member.dto.MemberInfoDto;
import com.joinproject.domain.member.dto.MemberSignUpDto;
import com.joinproject.domain.member.dto.MemberUpdateDto;
import com.joinproject.domain.member.repository.MemberRepository;
import com.joinproject.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = memberSignUpDto.toEntity();
        member.addUserAuthority();
        member.encodePassword(passwordEncoder);

        if (memberRepository.findByUsername(memberSignUpDto.getUsername()).isPresent()) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDto memberUpdateDto) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUserName()).orElseThrow(
                () -> new Exception("회원이 존재하지 않습니다.")
        );

        memberUpdateDto.getAge().ifPresent(member::updateAge);
        memberUpdateDto.getName().ifPresent(member::updateName);
        memberUpdateDto.getNickname().ifPresent(member::updateNickName);
    }

    @Override
    public void updatePassword(String checkPassword, String toBePassword) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUserName()).orElseThrow(
                () -> new Exception("회원이 존재하지 않습니다.")
        );

        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        member.updatePassword(passwordEncoder, toBePassword);
    }


    /**
     * 회원 탈퇴
     * */
    @Override
    public void withdraw(String checkPassword) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUserName()).orElseThrow(
                () -> new Exception("회원이 존재하지 않습니다.")
        );

        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
    }


    /**
     * id를 받아와서 해당 회원 정보 조회
     * */
    @Override
    public MemberInfoDto getInfo(Long id) throws Exception {
        Member findMember = memberRepository.findById(id).orElseThrow(
                () -> new Exception("회원이 없습니다.")
        );

        return new MemberInfoDto(findMember);
    }

    /**
     * 나의 정보 가져오기
     *
     * 로그인 한 경우, SecurityContextHolder에 들어있기 때문에 따로 입력받지 않아도
     * 인증만 되어있다면 정보 조회 가능
     * */
    @Override
    public MemberInfoDto getMyInfo() throws Exception {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getLoginUserName()).orElseThrow(
                () -> new Exception("회원이 존재하지 않습니다.")
        );

        return new MemberInfoDto(findMember);
    }
}
