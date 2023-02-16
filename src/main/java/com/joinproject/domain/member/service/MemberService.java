package com.joinproject.domain.member.service;

import com.joinproject.domain.member.dto.MemberInfoDto;
import com.joinproject.domain.member.dto.MemberSignUpDto;
import com.joinproject.domain.member.dto.MemberUpdateDto;

public interface MemberService {

    /**
     * 회원가입 CRUD
     * */
    void signUp(MemberSignUpDto memberSignUpDto) throws Exception;

    void update(MemberUpdateDto memberUpdateDto) throws Exception;

    void updatePassword(String checkPassword, String toBePassword) throws Exception;

    void withdraw(String checkPassword) throws Exception;

    MemberInfoDto getInfo(Long id) throws Exception;

    MemberInfoDto getMyInfo() throws Exception;

}
