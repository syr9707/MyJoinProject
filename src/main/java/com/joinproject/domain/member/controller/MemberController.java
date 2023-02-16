package com.joinproject.domain.member.controller;

import com.joinproject.domain.member.dto.*;
import com.joinproject.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/signUp")
    public void signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
    }

    // 회원정보 수정
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/member")
    public void updateBasicInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) throws Exception {
        memberService.update(memberUpdateDto);
    }

    // 비밀번호 수정
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/member/password")
    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.getCheckPassword(),updatePasswordDto.getToBePassword());
    }

    // 회원 탈퇴
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/member")
    public void withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.getCheckPassword());
    }

    // 회원정보 조회
    @GetMapping("/member/{id}")
    public ResponseEntity getInfo(@Valid @PathVariable("id") Long id) throws Exception {
        MemberInfoDto info = memberService.getInfo(id);
        return new ResponseEntity(info, HttpStatus.OK);
    }

    // 내 정보 조회
    @GetMapping("/member")
    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {
        MemberInfoDto info = memberService.getMyInfo();

        return new ResponseEntity(info, HttpStatus.OK);
    }
}
